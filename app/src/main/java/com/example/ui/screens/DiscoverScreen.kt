package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkoutData
import com.example.data.WorkoutExercise
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.AthleticBlue
import com.example.ui.theme.AthleticCoral
import com.example.ui.theme.AthleticGreen
import com.example.ui.theme.DimSlateText
import com.example.ui.theme.frostedGlassCard
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder

data class WorkoutCategory(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val exercises: List<String>
)

@Composable
fun DiscoverScreen(
    viewModel: WorkoutViewModel,
    onStartCategoryWorkout: (name: String, exercisesJson: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember {
        listOf(
            WorkoutCategory(
                id = "belly_fat",
                nameEn = "Belly Fat",
                nameBn = "পেটের মেদ কমানো",
                description = "Lose stubborn belly fat and trim your waistline.",
                icon = Icons.Default.Accessibility,
                color = AthleticCoral,
                exercises = listOf("Jumping Jack", "Bicycle Crunch", "Russian Twist", "Leg Raise", "Mountain Climber", "Plank", "Cobra Stretch")
            ),
            WorkoutCategory(
                id = "chest_fat",
                nameEn = "Chest Fat Burn",
                nameBn = "বুকের চর্বি কমানো",
                description = "Sculpt and tone your chest with bodyweight pushes.",
                icon = Icons.Default.FitnessCenter,
                color = AthleticBlue,
                exercises = listOf("Arm Circle", "Incline Push-up", "Push-up", "Wide Push-up", "Diamond Push-up", "Chest Stretch")
            ),
            WorkoutCategory(
                id = "abs",
                nameEn = "Abs Shredder",
                nameBn = "সিক্স প্যাক অ্যাবস",
                description = "Carve defined six-pack abs and strengthen your core.",
                icon = Icons.Default.GridOn,
                color = AthleticGreen,
                exercises = listOf("Jumping Jack", "Crunch", "Bicycle Crunch", "Leg Raise", "Russian Twist", "Plank")
            ),
            WorkoutCategory(
                id = "arms",
                nameEn = "Arm Strength",
                nameBn = "বাহু ও কাঁধের শক্তি",
                description = "Build muscular shoulders, biceps, and triceps.",
                icon = Icons.Default.Handyman,
                color = AthleticCoral,
                exercises = listOf("Arm Circle", "Incline Push-up", "Push-up", "Triceps Dip", "Diamond Push-up", "Cobra Stretch")
            ),
            WorkoutCategory(
                id = "legs",
                nameEn = "Legs Workout",
                nameBn = "পা ও থাই শক্তিশালী করা",
                description = "Tone your thighs, glutes, and calf muscles easily.",
                icon = Icons.Default.DirectionsWalk,
                color = AthleticGreen,
                exercises = listOf("Squat", "Lunges", "Glute Bridge", "Wall Sit", "Side Hop", "Child Pose")
            ),
            WorkoutCategory(
                id = "fat_burn",
                nameEn = "Fat Burn Blast",
                nameBn = "মেদ ঝরানো ব্লাস্ট",
                description = "High intensity cardio to melt body fat fast.",
                icon = Icons.Default.LocalFireDepartment,
                color = AthleticCoral,
                exercises = listOf("Jumping Jack", "Burpee", "High Knees", "Mountain Climber", "Side Hop", "Plank")
            ),
            WorkoutCategory(
                id = "hiit",
                nameEn = "HIIT Extreme",
                nameBn = "এইচআইআইটি এক্সট্রিম",
                description = "Interval training to boost endurance and metabolism.",
                icon = Icons.Default.OfflineBolt,
                color = AthleticBlue,
                exercises = listOf("Jumping Jack", "Burpee", "Mountain Climber", "High Knees", "Side Hop", "Plank", "Child Pose")
            ),
            WorkoutCategory(
                id = "warm_up",
                nameEn = "Warm Up",
                nameBn = "ব্যায়ামের প্রস্তুতি (Warm Up)",
                description = "Prepare your heart and muscles for active training.",
                icon = Icons.Default.TrendingUp,
                color = AthleticGreen,
                exercises = listOf("Arm Circle", "Jumping Jack", "Side Hop", "Chest Stretch")
            ),
            WorkoutCategory(
                id = "stretch",
                nameEn = "Post-Workout Stretch",
                nameBn = "স্ট্রেচিং ও ক্লান্তি দূরীকরণ",
                description = "Enhance flexibility and recover sore muscles.",
                icon = Icons.Default.SelfImprovement,
                color = AthleticBlue,
                exercises = listOf("Chest Stretch", "Cobra Stretch", "Child Pose")
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ক্যাটাগরি সমূহ (Workout Categories)",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "নির্দিষ্ট পেশীর ব্যায়াম বা বিশেষ সেশন বেছে নিন।",
            fontSize = 12.sp,
            color = DimSlateText,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .frostedGlassCard()
                        .clickable {
                            // Serialize exercise names as a simple comma-separated string to decode in Detail View
                            onStartCategoryWorkout(category.nameBn, category.exercises.joinToString(","))
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        category.color.copy(alpha = 0.08f),
                                        Color.Transparent
                                    )
                                )
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(category.color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = category.nameEn,
                                    tint = category.color,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = category.nameBn,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = category.description,
                                    fontSize = 11.sp,
                                    color = DimSlateText,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = "Count",
                                        tint = category.color,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${category.exercises.size} Exercises • No Equipment",
                                        fontSize = 10.sp,
                                        color = category.color,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Open",
                                tint = DimSlateText
                            )
                        }
                    }
                }
            }
        }
    }
}
