package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CustomWorkout
import com.example.data.WorkoutData
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.AthleticBlue
import com.example.ui.theme.AthleticCoral
import com.example.ui.theme.AthleticGreen
import com.example.ui.theme.DimSlateText
import com.example.ui.theme.frostedGlassCard
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomWorkoutScreen(
    viewModel: WorkoutViewModel,
    onNavigateToCustomDetail: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val customWorkouts by viewModel.customWorkouts.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    // Creator State
    var routineName by remember { mutableStateOf("") }
    var restTimeSeconds by remember { mutableIntStateOf(20) }
    val selectedExercises = remember { mutableStateListOf<String>() }

    val exerciseList = remember { WorkoutData.exerciseLibrary.keys.toList().sorted() }
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    routineName = ""
                    restTimeSeconds = 20
                    selectedExercises.clear()
                    showCreateDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("নতুন রুটিন তৈরি (CREATE)", fontWeight = FontWeight.Bold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 80.dp) // padding above navigation
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "আমার রুটিন সমূহ (Custom Workouts)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "আপনার নিজের পছন্দের ব্যায়ামগুলো একত্রিত করে কাস্টম রুটিন তৈরি করুন।",
                fontSize = 12.sp,
                color = DimSlateText,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (customWorkouts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LibraryAdd,
                            contentDescription = "Empty Custom",
                            tint = DimSlateText.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "কোনো কাস্টম রুটিন তৈরি করা নেই",
                            color = DimSlateText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "নিচের বাটনে চাপ দিয়ে প্রথম রুটিনটি তৈরি করুন।",
                            color = DimSlateText.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 120.dp) // Avoid FAB overlap
                ) {
                    items(customWorkouts) { workout ->
                        val exerciseCount = workout.exercisesString.split(",").filter { it.isNotEmpty() }.size
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .frostedGlassCard()
                                .clickable { onNavigateToCustomDetail(workout.id) },
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
                                        text = workout.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "ব্যায়াম সংখ্যা: $exerciseCount টি • বিশ্রাম: ${workout.restTimeSeconds}s",
                                        fontSize = 12.sp,
                                        color = DimSlateText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Exercises: " + workout.exercisesString.replace(",", ", "),
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.deleteCustomWorkout(workout.id) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = AthleticCoral
                                        )
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
    }

    // Interactive Routine Creator Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("নতুন কাস্টম রুটিন তৈরি") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = routineName,
                        onValueChange = { routineName = it },
                        label = { Text("রুটিনের নাম (Routine Name)") },
                        placeholder = { Text("যেমন: সকালের ব্যায়াম") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Rest Time Choice
                    Column {
                        Text(
                            text = "ব্যায়ামের মধ্যবর্তী বিশ্রাম সময়: ${restTimeSeconds}s",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf(15, 20, 25, 30).forEach { secs ->
                                val isSelected = restTimeSeconds == secs
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { restTimeSeconds = secs },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Text(
                                        text = "${secs}s",
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Choose exercises Checklist
                    Column {
                        Text(
                            text = "ব্যায়াম সমূহ নির্বাচন করুন (${selectedExercises.size}টি নির্বাচিত)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        exerciseList.forEach { exerciseName ->
                            val isChecked = selectedExercises.contains(exerciseName)
                            val bngName = WorkoutData.exerciseLibrary[exerciseName]?.descriptionBn?.split(" ")?.getOrNull(0) ?: ""
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isChecked) selectedExercises.remove(exerciseName)
                                        else selectedExercises.add(exerciseName)
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        if (checked == true) selectedExercises.add(exerciseName)
                                        else selectedExercises.remove(exerciseName)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = exerciseName, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                    Text(
                                        text = WorkoutData.getExercise(exerciseName).descriptionBn,
                                        fontSize = 11.sp,
                                        color = DimSlateText,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (routineName.isNotBlank() && selectedExercises.isNotEmpty()) {
                            viewModel.createCustomWorkout(
                                name = routineName,
                                restSeconds = restTimeSeconds,
                                exercises = selectedExercises.toList()
                            )
                            showCreateDialog = false
                        }
                    },
                    enabled = routineName.isNotBlank() && selectedExercises.isNotEmpty()
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
