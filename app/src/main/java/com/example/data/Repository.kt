package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(private val workoutDao: WorkoutDao) {

    val userProfile: Flow<UserProfile?> = workoutDao.getUserProfile()

    val weightLogs: Flow<List<WeightLog>> = workoutDao.getWeightLogs()

    val workoutHistory: Flow<List<WorkoutHistory>> = workoutDao.getWorkoutHistory()

    val customWorkouts: Flow<List<CustomWorkout>> = workoutDao.getCustomWorkouts()

    suspend fun saveUserProfile(profile: UserProfile) {
        workoutDao.insertUserProfile(profile)
    }

    suspend fun addWeightLog(weight: Float) {
        val log = WeightLog(
            timestamp = System.currentTimeMillis(),
            weight = weight
        )
        workoutDao.insertWeightLog(log)
    }

    suspend fun deleteWeightLog(id: Int) {
        workoutDao.deleteWeightLog(id)
    }

    suspend fun addWorkoutHistory(dayIndex: Int, workoutName: String, durationSeconds: Int, caloriesBurned: Int) {
        val history = WorkoutHistory(
            timestamp = System.currentTimeMillis(),
            dayIndex = dayIndex,
            workoutName = workoutName,
            durationSeconds = durationSeconds,
            caloriesBurned = caloriesBurned
        )
        workoutDao.insertWorkoutHistory(history)
    }

    suspend fun addCustomWorkout(name: String, restTimeSeconds: Int, exercises: List<String>) {
        val customWorkout = CustomWorkout(
            name = name,
            restTimeSeconds = restTimeSeconds,
            exercisesString = exercises.joinToString(",")
        )
        workoutDao.insertCustomWorkout(customWorkout)
    }

    suspend fun deleteCustomWorkout(id: Int) {
        workoutDao.deleteCustomWorkout(id)
    }
}
