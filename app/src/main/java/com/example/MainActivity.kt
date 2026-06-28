package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.WorkoutData
import com.example.data.WorkoutExercise
import com.example.ui.WorkoutViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.meshGradientBackground
import com.example.ui.theme.GlassNavBg
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: WorkoutViewModel = viewModel()
                val userProfile by viewModel.userProfile.collectAsState()
                val activeSession by viewModel.activeSession.collectAsState()

                // Navigation State routing
                var currentTab by remember { mutableIntStateOf(0) }
                var detailDayIndex by remember { mutableStateOf<Int?>(null) }
                var customDetailId by remember { mutableStateOf<Int?>(null) }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .meshGradientBackground(),
                    color = Color.Transparent
                ) {
                    when {
                        // 1. Onboarding Screen
                        !userProfile.isOnboarded -> {
                            OnboardingScreen(
                                onOnboardingComplete = { name, age, height, currentWeight, targetWeight, goal, fitnessLevel, lowImpactMode ->
                                    viewModel.completeOnboarding(
                                        name = name,
                                        age = age,
                                        height = height,
                                        currentWeight = currentWeight,
                                        targetWeight = targetWeight,
                                        goal = goal,
                                        fitnessLevel = fitnessLevel,
                                        lowImpactMode = lowImpactMode
                                    )
                                }
                            )
                        }

                        // 2. Active Workout Timer Overlay
                        activeSession != null -> {
                            WorkoutTimerScreen(
                                viewModel = viewModel,
                                onWorkoutFinished = {
                                    detailDayIndex = null
                                    customDetailId = null
                                    currentTab = 3 // Move to statistics tab to see history and calories
                                }
                            )
                        }

                        // 3. Standard 30-Day Workout Detail view
                        detailDayIndex != null -> {
                            WorkoutDetailScreen(
                                dayIndex = detailDayIndex!!,
                                customWorkoutId = -1,
                                viewModel = viewModel,
                                onNavigateBack = { detailDayIndex = null },
                                onStartWorkout = { /* Started, viewmodel triggers timer overlay automatically */ }
                            )
                        }

                        // 4. Custom Workout Detail view
                        customDetailId != null -> {
                            WorkoutDetailScreen(
                                dayIndex = -1,
                                customWorkoutId = customDetailId!!,
                                viewModel = viewModel,
                                onNavigateBack = { customDetailId = null },
                                onStartWorkout = { /* Started, viewmodel triggers timer overlay automatically */ }
                            )
                        }

                        // 5. Main Dashboard Tabs Scaffold
                        else -> {
                            Scaffold(
                                containerColor = Color.Transparent,
                                bottomBar = {
                                    NavigationBar(
                                        containerColor = Color.Transparent,
                                        modifier = Modifier.background(GlassNavBg),
                                        tonalElevation = 0.dp
                                    ) {
                                        NavigationBarItem(
                                            selected = currentTab == 0,
                                            onClick = { currentTab = 0 },
                                            icon = {
                                                Icon(
                                                    imageVector = if (currentTab == 0) Icons.Default.CalendarMonth else Icons.Outlined.CalendarMonth,
                                                    contentDescription = "Plan"
                                                )
                                            },
                                            label = { Text("পরিকল্পনা (Plan)", fontSize = 10.sp) }
                                        )

                                        NavigationBarItem(
                                            selected = currentTab == 1,
                                            onClick = { currentTab = 1 },
                                            icon = {
                                                Icon(
                                                    imageVector = if (currentTab == 1) Icons.Default.Explore else Icons.Outlined.Explore,
                                                    contentDescription = "Discover"
                                                )
                                            },
                                            label = { Text("আবিষ্কার (Discover)", fontSize = 10.sp) }
                                        )

                                        NavigationBarItem(
                                            selected = currentTab == 2,
                                            onClick = { currentTab = 2 },
                                            icon = {
                                                Icon(
                                                    imageVector = if (currentTab == 2) Icons.Default.Handyman else Icons.Outlined.Handyman,
                                                    contentDescription = "Custom"
                                                )
                                            },
                                            label = { Text("রুটিন (Custom)", fontSize = 10.sp) }
                                        )

                                        NavigationBarItem(
                                            selected = currentTab == 3,
                                            onClick = { currentTab = 3 },
                                            icon = {
                                                Icon(
                                                    imageVector = if (currentTab == 3) Icons.Default.Insights else Icons.Outlined.Insights,
                                                    contentDescription = "Stats"
                                                )
                                            },
                                            label = { Text("অগ্রগতি (Stats)", fontSize = 10.sp) }
                                        )
                                    }
                                }
                            ) { innerPadding ->
                                when (currentTab) {
                                    0 -> HomeScreen(
                                        viewModel = viewModel,
                                        onNavigateToDetail = { index, _ -> detailDayIndex = index },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                    1 -> DiscoverScreen(
                                        viewModel = viewModel,
                                        onStartCategoryWorkout = { name, exercisesJson ->
                                            val names = exercisesJson.split(",").filter { it.isNotEmpty() }
                                            val workoutExercises = names.map { exName ->
                                                val ex = WorkoutData.getExercise(exName)
                                                WorkoutExercise(
                                                    exercise = ex,
                                                    targetValue = if (ex.isDurationBased) 30 else 12,
                                                    restSeconds = 15
                                                )
                                            }
                                            viewModel.startWorkout(
                                                dayIndex = -1,
                                                name = name,
                                                exercises = workoutExercises
                                            )
                                        },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                    2 -> CustomWorkoutScreen(
                                        viewModel = viewModel,
                                        onNavigateToCustomDetail = { id -> customDetailId = id },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                    3 -> StatisticsScreen(
                                        viewModel = viewModel,
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
