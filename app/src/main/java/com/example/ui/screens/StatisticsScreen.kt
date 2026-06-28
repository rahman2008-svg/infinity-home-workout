package com.example.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WeightLog
import com.example.data.WorkoutHistory
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.AthleticBlue
import com.example.ui.theme.AthleticCoral
import com.example.ui.theme.AthleticGreen
import com.example.ui.theme.DimSlateText
import com.example.ui.theme.frostedGlassCard
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder
import java.util.Date

@Composable
fun StatisticsScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val weightLogs by viewModel.weightLogs.collectAsState()
    val workoutHistory by viewModel.workoutHistory.collectAsState()

    var showWeightDialog by remember { mutableStateOf(false) }
    var weightInput by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Weight Logs, 1: Workout History

    // Calculate BMI
    val heightM = userProfile.height / 100f
    val bmi = if (heightM > 0) userProfile.currentWeight / (heightM * heightM) else 0f
    val bmiCategory = when {
        bmi < 18.5f -> Pair("Underweight (কম ওজন)", AthleticCoral)
        bmi < 25.0f -> Pair("Normal Weight (স্বাভাবিক ওজন)", AthleticGreen)
        bmi < 30.0f -> Pair("Overweight (অতিরিক্ত ওজন)", AthleticCoral)
        else -> Pair("Obese (স্থূলতা)", AthleticCoral)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Statistics Title
        item {
            Text(
                text = "আমার অগ্রগতি (My Progress & Tracker)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "আপনার প্রতিদিনের ওজন এবং ব্যায়ামের অগ্রগতি ট্র্যাক করুন।",
                fontSize = 12.sp,
                color = DimSlateText
            )
        }

        // BMI Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .frostedGlassCard(),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("বর্তমান বিএমআই (BMI)", fontSize = 11.sp, color = DimSlateText, fontWeight = FontWeight.Bold)
                            Text(
                                text = String.format("%.1f", bmi),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = bmiCategory.second.copy(alpha = 0.15f))
                        ) {
                            Text(
                                text = bmiCategory.first,
                                color = bmiCategory.second,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress bar showing current weight against target
                    val progress = remember(userProfile) {
                        val totalDiff = Math.abs(userProfile.currentWeight - userProfile.targetWeight)
                        // Simple indicator of target proximity
                        if (userProfile.currentWeight > userProfile.targetWeight) {
                            (1f - (totalDiff / userProfile.currentWeight)).coerceIn(0.1f, 1.0f)
                        } else {
                            1.0f
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = AthleticGreen,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "বর্তমান: ${userProfile.currentWeight} kg",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "লক্ষ্য: ${userProfile.targetWeight} kg",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AthleticBlue
                        )
                    }
                }
            }
        }

        // Custom Weight Trend Chart
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .frostedGlassCard(),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ওজনের চার্ট (Weight Trend)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = {
                                weightInput = userProfile.currentWeight.toString()
                                showWeightDialog = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Add, contentDescription = "Log", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ওজন লগ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (weightLogs.size < 2) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "গ্রাফ দেখতে অন্তত ২টি ওজনের ডেটা এন্ট্রি করুন।",
                                color = DimSlateText,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Custom Canvas Line Graph
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
                            val width = size.width
                            val height = size.height

                            val minWeight = weightLogs.minOf { it.weight } - 2f
                            val maxWeight = weightLogs.maxOf { it.weight } + 2f
                            val weightRange = maxWeight - minWeight

                            val pointsCount = weightLogs.size
                            val stepX = width / (pointsCount - 1).coerceAtLeast(1)

                            val path = Path()
                            val fillPath = Path()

                            // Draw reference grid lines
                            val gridLines = 3
                            for (i in 0..gridLines) {
                                val gridY = height * i / gridLines
                                drawLine(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    start = androidx.compose.ui.geometry.Offset(0f, gridY),
                                    end = androidx.compose.ui.geometry.Offset(width, gridY),
                                    strokeWidth = 2f
                                )
                            }

                            weightLogs.forEachIndexed { index, log ->
                                val x = index * stepX
                                val normalizedY = (log.weight - minWeight) / weightRange
                                val y = height - (normalizedY * height)

                                if (index == 0) {
                                    path.moveTo(x, y)
                                    fillPath.moveTo(x, height)
                                    fillPath.lineTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                    fillPath.lineTo(x, y)
                                }

                                if (index == pointsCount - 1) {
                                    fillPath.lineTo(x, height)
                                    fillPath.close()
                                }

                                // Draw glowing dot
                                drawCircle(
                                    color = AthleticBlue,
                                    radius = 5.dp.toPx(),
                                    center = androidx.compose.ui.geometry.Offset(x, y)
                                )
                                drawCircle(
                                    color = Color.White,
                                    radius = 2.dp.toPx(),
                                    center = androidx.compose.ui.geometry.Offset(x, y)
                                )
                            }

                            // Draw gradient stroke line
                            drawPath(
                                path = path,
                                color = AthleticBlue,
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Draw area fill gradient
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        AthleticBlue.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

        // Logs and History segmented tab
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("ওজন হিস্ট্রি (${weightLogs.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("ওয়ার্কআউট হিস্ট্রি (${workoutHistory.size})", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Render based on selected Tab
        if (selectedTab == 0) {
            if (weightLogs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("কোনো ওজন হিস্ট্রি রেকর্ড নেই", color = DimSlateText)
                    }
                }
            } else {
                items(weightLogs.reversed()) { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlassCard(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(AthleticBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.MonitorWeight, contentDescription = "Weight", tint = AthleticBlue)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "${log.weight} kg",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    val dateStr = DateFormat.format("dd MMM yyyy, hh:mm a", Date(log.timestamp)).toString()
                                    Text(
                                        text = dateStr,
                                        fontSize = 11.sp,
                                        color = DimSlateText
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.deleteWeightLog(log.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AthleticCoral)
                            }
                        }
                    }
                }
            }
        } else {
            if (workoutHistory.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("কোনো ব্যায়াম সম্পন্ন করার ইতিহাস নেই", color = DimSlateText)
                    }
                }
            } else {
                items(workoutHistory) { history ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlassCard(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(AthleticGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Checked", tint = AthleticGreen)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = history.workoutName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    val mins = history.durationSeconds / 60
                                    Text(
                                        text = "$mins mins • ${history.caloriesBurned} KCAL",
                                        fontSize = 12.sp,
                                        color = DimSlateText
                                    )
                                    val dateStr = DateFormat.format("dd MMM yyyy, hh:mm a", Date(history.timestamp)).toString()
                                    Text(
                                        text = dateStr,
                                        fontSize = 10.sp,
                                        color = DimSlateText.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            if (history.dayIndex in 1..30) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = AthleticGreen.copy(alpha = 0.15f))
                                ) {
                                    Text(
                                        text = "DAY ${history.dayIndex}",
                                        color = AthleticGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Weight Logger Dialog
    if (showWeightDialog) {
        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            title = { Text("নতুন ওজন যোগ করুন (Log Weight)") },
            text = {
                Column {
                    Text("আপনার বর্তমান ওজন লিখুন (কেজিতে)। এটি চার্ট এবং বিএমআই-তে স্বয়ংক্রিয়ভাবে আপডেট হবে।")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("বর্তমান ওজন (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = weightInput.toFloatOrNull()
                        if (parsed != null && parsed > 0) {
                            viewModel.logWeight(parsed)
                            showWeightDialog = false
                        }
                    }
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWeightDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
