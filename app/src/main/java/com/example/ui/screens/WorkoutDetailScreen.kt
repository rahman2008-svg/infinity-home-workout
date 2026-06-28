package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkoutData
import com.example.data.WorkoutExercise
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.AthleticBlue
import com.example.ui.theme.AthleticCoral
import com.example.ui.theme.DimSlateText
import com.example.ui.theme.frostedGlassCard
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    dayIndex: Int,
    customWorkoutId: Int = -1,
    viewModel: WorkoutViewModel,
    onNavigateBack: () -> Unit,
    onStartWorkout: () -> Unit
) {
    val thirtyDayPlan = remember { WorkoutData.get30DayPlan() }
    val customWorkouts by viewModel.customWorkouts.collectAsState()

    // Resolve the active workout exercises and title
    val (title, subTitle, exercises) = remember(dayIndex, customWorkoutId, customWorkouts) {
        if (customWorkoutId != -1) {
            val custom = customWorkouts.find { it.id == customWorkoutId }
            if (custom != null) {
                val exerciseNames = custom.exercisesString.split(",").filter { it.isNotEmpty() }
                val workoutExercises = exerciseNames.map { name ->
                    val ex = WorkoutData.getExercise(name)
                    WorkoutExercise(
                        exercise = ex,
                        targetValue = if (ex.isDurationBased) 30 else 12,
                        restSeconds = custom.restTimeSeconds
                    )
                }
                Triple(custom.name, "Custom Workout Routine", workoutExercises)
            } else {
                Triple("Workout Not Found", "", emptyList())
            }
        } else {
            val day = thirtyDayPlan.find { it.dayIndex == dayIndex } ?: thirtyDayPlan.first()
            Triple(day.title, "${day.focusAreaBn} (${day.focusArea})", day.exercises)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            if (exercises.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.startWorkout(
                            dayIndex = if (customWorkoutId != -1) -1 else dayIndex,
                            name = title,
                            exercises = exercises
                        )
                        onStartWorkout()
                    },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Start") },
                    text = { Text("শুরু করুন (START)", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        if (exercises.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("কোনো ব্যায়াম খুঁজে পাওয়া যায়নি।")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Intro Header card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlassCard(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = subTitle,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column {
                                    Text("ব্যায়াম সংখ্যা", fontSize = 11.sp, color = DimSlateText)
                                    Text("${exercises.size}টি", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Column {
                                    Text("আনুমানিক সময়", fontSize = 11.sp, color = DimSlateText)
                                    val estMins = if (customWorkoutId != -1) exercises.size else {
                                        thirtyDayPlan.find { it.dayIndex == dayIndex }?.durationMinutes ?: (exercises.size * 1.5).toInt()
                                    }
                                    Text("$estMins মিনিট", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AthleticBlue)
                                }
                                Column {
                                    Text("যন্ত্রপাতি", fontSize = 11.sp, color = DimSlateText)
                                    Text("কোনোটির প্রয়োজন নেই", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                }

                // Exercises List Section Title
                item {
                    Text(
                        text = "ব্যায়াম সমূহ (${exercises.size} Exercises)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Exercise Items
                items(exercises) { we ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlassCard(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Icon Indicator
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "Exercise",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Details Center
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = we.exercise.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = we.exercise.descriptionBn,
                                    fontSize = 12.sp,
                                    color = DimSlateText,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Reps / Duration count right side
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (we.exercise.isDurationBased) AthleticCoral.copy(alpha = 0.1f)
                                    else AthleticBlue.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (we.exercise.isDurationBased) "${we.targetValue}s" else "x${we.targetValue}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = if (we.exercise.isDurationBased) AthleticCoral else AthleticBlue,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Extra spacing at the bottom to avoid FAB occlusion
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
