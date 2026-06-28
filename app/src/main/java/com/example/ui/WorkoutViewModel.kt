package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ActiveWorkoutSession(
    val dayIndex: Int, // 1-30, or -1 for custom/discover
    val workoutName: String,
    val exercises: List<WorkoutExercise>,
    val currentExerciseIndex: Int = 0,
    val secondsRemaining: Int = 0,
    val isResting: Boolean = false,
    val isPaused: Boolean = false,
    val isCompleted: Boolean = false
)

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val userProfile: StateFlow<UserProfile>
    val weightLogs: StateFlow<List<WeightLog>>
    val workoutHistory: StateFlow<List<WorkoutHistory>>
    val customWorkouts: StateFlow<List<CustomWorkout>>

    // Active workout state
    private val _activeSession = MutableStateFlow<ActiveWorkoutSession?>(null)
    val activeSession: StateFlow<ActiveWorkoutSession?> = _activeSession.asStateFlow()

    private var timerJob: Job? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = Repository(database.workoutDao())

        // Setup flows with stateIn
        userProfile = repository.userProfile
            .map { it ?: UserProfile() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserProfile()
            )

        weightLogs = repository.weightLogs
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        workoutHistory = repository.workoutHistory
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        customWorkouts = repository.customWorkouts
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    // --- Profile Operations ---
    fun updateUserProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun completeOnboarding(
        name: String,
        age: Int,
        height: Float,
        currentWeight: Float,
        targetWeight: Float,
        goal: String,
        fitnessLevel: String,
        lowImpactMode: Boolean
    ) {
        viewModelScope.launch {
            val initialProfile = UserProfile(
                name = name,
                age = age,
                height = height,
                currentWeight = currentWeight,
                targetWeight = targetWeight,
                goal = goal,
                fitnessLevel = fitnessLevel,
                lowImpactMode = lowImpactMode,
                isOnboarded = true
            )
            repository.saveUserProfile(initialProfile)
            // Log initial weight
            repository.addWeightLog(currentWeight)
        }
    }

    // --- Weight Logging ---
    fun logWeight(weight: Float) {
        viewModelScope.launch {
            repository.addWeightLog(weight)
            // Also update current weight in profile
            val updatedProfile = userProfile.value.copy(currentWeight = weight)
            repository.saveUserProfile(updatedProfile)
        }
    }

    fun deleteWeightLog(id: Int) {
        viewModelScope.launch {
            repository.deleteWeightLog(id)
        }
    }

    // --- Custom Workouts ---
    fun createCustomWorkout(name: String, restSeconds: Int, exercises: List<String>) {
        viewModelScope.launch {
            repository.addCustomWorkout(name, restSeconds, exercises)
        }
    }

    fun deleteCustomWorkout(id: Int) {
        viewModelScope.launch {
            repository.deleteCustomWorkout(id)
        }
    }

    // --- Streak & Progress Calculations ---
    val streakDays: StateFlow<Int> = workoutHistory.map { history ->
        calculateStreak(history)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCaloriesBurned: StateFlow<Int> = workoutHistory.map { history ->
        history.sumOf { it.caloriesBurned }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMinutesWorkout: StateFlow<Int> = workoutHistory.map { history ->
        history.sumOf { it.durationSeconds } / 60
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedDaysSet: StateFlow<Set<Int>> = workoutHistory.map { history ->
        history.filter { it.dayIndex in 1..30 }.map { it.dayIndex }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private fun calculateStreak(history: List<WorkoutHistory>): Int {
        if (history.isEmpty()) return 0
        // Get unique dates of workouts in milliseconds (ignoring time)
        val msInDay = 24 * 60 * 60 * 1000L
        val workoutDates = history.map {
            it.timestamp / msInDay
        }.distinct().sortedDescending()

        if (workoutDates.isEmpty()) return 0

        val todayDate = System.currentTimeMillis() / msInDay
        val latestWorkoutDate = workoutDates.first()

        // If the latest workout is older than yesterday, streak is broken (0)
        if (todayDate - latestWorkoutDate > 1) {
            return 0
        }

        var streak = 1
        for (i in 0 until workoutDates.size - 1) {
            if (workoutDates[i] - workoutDates[i + 1] == 1L) {
                streak++
            } else if (workoutDates[i] - workoutDates[i + 1] > 1L) {
                break
            }
        }
        return streak
    }

    // --- Active Workout Engine ---
    fun startWorkout(dayIndex: Int, name: String, exercises: List<WorkoutExercise>) {
        val filteredExercises = if (userProfile.value.lowImpactMode) {
            // In low impact mode, map high-impact exercises (like Burpees, Jumping Jacks) to lower impact versions
            exercises.map { we ->
                val lowImpactExercise = when (we.exercise.name) {
                    "Burpee" -> WorkoutData.getExercise("Incline Push-up")
                    "Jumping Jack" -> WorkoutData.getExercise("Side Hop")
                    "High Knees" -> WorkoutData.getExercise("Side Hop")
                    "Mountain Climber" -> WorkoutData.getExercise("Wall Sit")
                    else -> we.exercise
                }
                we.copy(exercise = lowImpactExercise)
            }
        } else {
            exercises
        }

        if (filteredExercises.isEmpty()) return

        timerJob?.cancel()
        
        val firstExercise = filteredExercises.first()
        val duration = if (firstExercise.exercise.isDurationBased) firstExercise.targetValue else 15 // Countdown/preparation timer or base duration

        _activeSession.value = ActiveWorkoutSession(
            dayIndex = dayIndex,
            workoutName = name,
            exercises = filteredExercises,
            currentExerciseIndex = 0,
            secondsRemaining = duration,
            isResting = false,
            isPaused = false,
            isCompleted = false
        )

        runTimer()
    }

    fun togglePauseResume() {
        _activeSession.value = _activeSession.value?.let {
            it.copy(isPaused = !it.isPaused)
        }
    }

    fun skipCurrentExercise() {
        val session = _activeSession.value ?: return
        moveToNextStep(session)
    }

    fun restExtraSeconds(seconds: Int) {
        _activeSession.value = _activeSession.value?.let {
            if (it.isResting) {
                it.copy(secondsRemaining = it.secondsRemaining + seconds)
            } else {
                it
            }
        }
    }

    private fun runTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val session = _activeSession.value ?: break
                if (session.isPaused || session.isCompleted) continue

                if (session.secondsRemaining > 1) {
                    _activeSession.value = session.copy(secondsRemaining = session.secondsRemaining - 1)
                } else {
                    // Current step finished
                    moveToNextStep(session)
                }
            }
        }
    }

    private fun moveToNextStep(session: ActiveWorkoutSession) {
        val currentIndex = session.currentExerciseIndex
        val currentEx = session.exercises[currentIndex]

        if (!session.isResting && currentIndex < session.exercises.size - 1) {
            // Exercise ended -> Go to rest
            _activeSession.value = session.copy(
                isResting = true,
                secondsRemaining = currentEx.restSeconds
            )
        } else if (session.isResting) {
            // Rest ended -> Go to next exercise
            val nextIndex = currentIndex + 1
            val nextEx = session.exercises[nextIndex]
            val duration = if (nextEx.exercise.isDurationBased) nextEx.targetValue else 15 // Set prep/exercise seconds
            
            _activeSession.value = session.copy(
                currentExerciseIndex = nextIndex,
                isResting = false,
                secondsRemaining = duration
            )
        } else {
            // Last exercise ended without rest -> Workout Completed!
            timerJob?.cancel()
            _activeSession.value = session.copy(
                isCompleted = true,
                secondsRemaining = 0
            )
        }
    }

    fun completeAndSaveWorkout() {
        val session = _activeSession.value ?: return
        viewModelScope.launch {
            // Calculate actual minutes and calories
            val durationSeconds = session.exercises.sumOf { 
                if (it.exercise.isDurationBased) it.targetValue else 15 // reps take approx 15s for stats
            }
            val calories = session.exercises.sumOf { we ->
                val durationMins = (if (we.exercise.isDurationBased) we.targetValue else 15).toFloat() / 60f
                (durationMins * we.exercise.caloriesPerMinute).toInt()
            }.coerceAtLeast(10) // minimum 10 calories

            repository.addWorkoutHistory(
                dayIndex = session.dayIndex,
                workoutName = session.workoutName,
                durationSeconds = durationSeconds,
                caloriesBurned = calories
            )

            // Auto-log weight for progress if requested
            _activeSession.value = null
        }
    }

    fun cancelWorkout() {
        timerJob?.cancel()
        _activeSession.value = null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
