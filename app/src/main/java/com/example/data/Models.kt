package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val age: Int = 25,
    val height: Float = 170f, // in cm
    val currentWeight: Float = 70f, // in kg
    val targetWeight: Float = 65f, // in kg
    val fitnessLevel: String = "Beginner", // Beginner, Intermediate, Advanced
    val goal: String = "Lose Weight", // Lose Weight, Lose Belly Fat, Chest Fat, Six Pack, Full Body Fit
    val lowImpactMode: Boolean = false,
    val isOnboarded: Boolean = false,
    val dailyReminderHour: Int = 8,
    val dailyReminderMinute: Int = 0,
    val dailyReminderEnabled: Boolean = true,
    val googleFitSyncEnabled: Boolean = false
)

@Entity(tableName = "weight_logs")
data class WeightLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val weight: Float
)

@Entity(tableName = "workout_history")
data class WorkoutHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val dayIndex: Int, // 1 to 30, or -1 for custom/discover workouts
    val workoutName: String,
    val durationSeconds: Int,
    val caloriesBurned: Int
)

@Entity(tableName = "custom_workouts")
data class CustomWorkout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val restTimeSeconds: Int = 20,
    val exercisesString: String // Comma separated names of exercises
)
