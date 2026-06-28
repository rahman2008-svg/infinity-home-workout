package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.data.WorkoutData
import com.example.data.WorkoutDay
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.AthleticBlue
import com.example.ui.theme.AthleticCoral
import com.example.ui.theme.AthleticGreen
import com.example.ui.theme.DimSlateText
import com.example.ui.theme.frostedGlassCard
import com.example.ui.theme.frostedGlassHero
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder

@Composable
fun HomeScreen(
    viewModel: WorkoutViewModel,
    onNavigateToDetail: (dayIndex: Int, workoutName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val completedDays by viewModel.completedDaysSet.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val totalCalories by viewModel.totalCaloriesBurned.collectAsState()
    val totalMinutes by viewModel.totalMinutesWorkout.collectAsState()

    val context = LocalContext.current
    var showReminderDialog by remember { mutableStateOf(false) }

    // Calculate current day to focus on
    val thirtyDayPlan = remember { WorkoutData.get30DayPlan() }
    val todayWorkoutDay = remember(completedDays) {
        val nextDayIndex = (1..30).firstOrNull { it !in completedDays } ?: 30
        thirtyDayPlan.firstOrNull { it.dayIndex == nextDayIndex } ?: thirtyDayPlan.first()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // App header title
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "INFINITY",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = AthleticBlue,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "HOME WORKOUT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                }

                // Quick settings row: Low Impact indicator and reminder icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            viewModel.updateUserProfile(
                                userProfile.copy(lowImpactMode = !userProfile.lowImpactMode)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (userProfile.lowImpactMode) Icons.Default.Accessible else Icons.Default.DirectionsRun,
                            contentDescription = "Low Impact Mode",
                            tint = if (userProfile.lowImpactMode) AthleticGreen else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }

                    IconButton(onClick = { showReminderDialog = true }) {
                        Icon(
                            imageVector = if (userProfile.dailyReminderEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                            contentDescription = "Reminder Settings",
                            tint = if (userProfile.dailyReminderEnabled) AthleticBlue else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // Stats Card Grid Row
        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .frostedGlassCard(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Calories",
                            tint = AthleticCoral,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalCalories",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "KCAL",
                            fontSize = 10.sp,
                            color = DimSlateText,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.OfflineBolt,
                            contentDescription = "Streak",
                            tint = AthleticGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$streakDays",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "DAYS STREAK",
                            fontSize = 10.sp,
                            color = DimSlateText,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Minutes",
                            tint = AthleticBlue,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalMinutes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "MINUTES",
                            fontSize = 10.sp,
                            color = DimSlateText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Today's Recommended Workout Banner Card
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "আজকের ব্যায়াম (Today's Workout)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .frostedGlassHero()
                        .clickable {
                            onNavigateToDetail(todayWorkoutDay.dayIndex, todayWorkoutDay.title)
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Day ${todayWorkoutDay.dayIndex}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = todayWorkoutDay.focusAreaBn,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${todayWorkoutDay.focusArea} • ${todayWorkoutDay.durationMinutes} Mins",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }

                            Button(
                                onClick = {
                                    onNavigateToDetail(todayWorkoutDay.dayIndex, todayWorkoutDay.title)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("শুরু করুন", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // 30 Days Workout Plan Grid title
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "৩০ দিনের ব্যায়াম পরিকল্পনা (30-Day Plan)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${completedDays.size}/30 DAYS COMPLETED",
                        fontSize = 11.sp,
                        color = AthleticBlue,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                if (completedDays.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            // Reset plan unit test/development convenience
                            viewModel.updateUserProfile(userProfile.copy(isOnboarded = true))
                        }
                    ) {
                        Text("Reset Plan", fontSize = 12.sp, color = AthleticCoral)
                    }
                }
            }
        }

        // 30 Grid items (Days 1 to 30)
        items(thirtyDayPlan.size) { index ->
            val day = thirtyDayPlan[index]
            val isCompleted = day.dayIndex in completedDays

            val cardModifier = if (isCompleted) {
                Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .frostedGlassCard(
                        backgroundColor = AthleticGreen.copy(alpha = 0.15f),
                        borderColor = AthleticGreen
                    )
                    .clickable {
                        onNavigateToDetail(day.dayIndex, day.title)
                    }
            } else {
                Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .frostedGlassCard()
                    .clickable {
                        onNavigateToDetail(day.dayIndex, day.title)
                    }
            }

            Card(
                modifier = cardModifier,
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = AthleticGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "DAY",
                                fontSize = 10.sp,
                                color = DimSlateText,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${day.dayIndex}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        Text(
                            text = day.focusAreaBn.split(" ")[0], // Get first word for short fitting
                            fontSize = 9.sp,
                            color = if (isCompleted) AthleticGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Low Impact Mode detail Banner Card
        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .frostedGlassCard(
                        backgroundColor = if (userProfile.lowImpactMode) AthleticGreen.copy(alpha = 0.12f) else GlassCardBg,
                        borderColor = if (userProfile.lowImpactMode) AthleticGreen.copy(alpha = 0.5f) else GlassCardBorder
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (userProfile.lowImpactMode) "সহজ মোড সক্রিয় (Low Impact Mode ON)" else "সহজ মোড নিষ্ক্রিয় (Low Impact Mode OFF)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (userProfile.lowImpactMode) AthleticGreen else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "সক্রিয় থাকলে জাম্পিং জ্যাক ও ভারী ব্যায়াম পরিবর্তন করে হাঁটু ও জয়েন্ট-বান্ধব ব্যায়াম দেওয়া হয়।",
                            fontSize = 11.sp,
                            color = DimSlateText,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = userProfile.lowImpactMode,
                        onCheckedChange = { isEnabled ->
                            viewModel.updateUserProfile(userProfile.copy(lowImpactMode = isEnabled))
                        }
                    )
                }
            }
        }
    }

    // Daily Reminder Setting Dialog
    if (showReminderDialog) {
        AlertDialog(
            onDismissRequest = { showReminderDialog = false },
            title = { Text("দৈনিক রিমাইন্ডার (Workout Reminder)") },
            text = {
                Column {
                    Text(
                        text = "ব্যায়াম করার জন্য একটি নির্দিষ্ট সময় নির্ধারণ করুন। সময় অনুযায়ী অ্যাপ আপনাকে নোটিফিকেশন পাঠাবে।",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("রিমাইন্ডার চালু করুন", fontWeight = FontWeight.Bold)
                        Switch(
                            checked = userProfile.dailyReminderEnabled,
                            onCheckedChange = { isEnabled ->
                                viewModel.updateUserProfile(userProfile.copy(dailyReminderEnabled = isEnabled))
                            }
                        )
                    }

                    if (userProfile.dailyReminderEnabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "নির্ধারিত সময়: ${String.format("%02d:%02d", userProfile.dailyReminderHour, userProfile.dailyReminderMinute)} ${if (userProfile.dailyReminderHour < 12) "AM" else "PM"}",
                            fontWeight = FontWeight.Bold,
                            color = AthleticBlue,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            TextButton(onClick = {
                                viewModel.updateUserProfile(userProfile.copy(dailyReminderHour = 7, dailyReminderMinute = 0))
                            }) { Text("সকাল ৭টা") }
                            TextButton(onClick = {
                                viewModel.updateUserProfile(userProfile.copy(dailyReminderHour = 17, dailyReminderMinute = 30))
                            }) { Text("বিকাল ৫:৩০") }
                            TextButton(onClick = {
                                viewModel.updateUserProfile(userProfile.copy(dailyReminderHour = 20, dailyReminderMinute = 0))
                            }) { Text("রাত ৮টা") }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showReminderDialog = false }) {
                    Text("সংরক্ষণ করুন")
                }
            }
        )
    }
}
