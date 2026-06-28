package com.example.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkoutExercise
import com.example.ui.components.ExerciseAnimation
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.AthleticBlue
import com.example.ui.theme.AthleticCoral
import com.example.ui.theme.AthleticGreen
import com.example.ui.theme.DimSlateText
import com.example.ui.theme.frostedGlassCard
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder
import java.util.Locale

@Composable
fun WorkoutTimerScreen(
    viewModel: WorkoutViewModel,
    onWorkoutFinished: () -> Unit
) {
    val activeSession by viewModel.activeSession.collectAsState()
    val context = LocalContext.current

    // Initialize TTS for voice guidance
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                ttsReady = true
            }
        }
    }

    // Voice announcement when state shifts
    LaunchedEffect(activeSession?.currentExerciseIndex, activeSession?.isResting, activeSession?.isCompleted) {
        val session = activeSession ?: return@LaunchedEffect
        if (ttsReady && tts != null) {
            val sentence = when {
                session.isCompleted -> "Congratulations! Workout completed."
                session.isResting -> {
                    val nextEx = session.exercises.getOrNull(session.currentExerciseIndex + 1)
                    if (nextEx != null) "Rest. Up next, ${nextEx.exercise.name}." else "Rest."
                }
                else -> {
                    val currentEx = session.exercises[session.currentExerciseIndex]
                    if (currentEx.exercise.isDurationBased) {
                        "Start ${currentEx.exercise.name} for ${currentEx.targetValue} seconds."
                    } else {
                        "Start ${currentEx.exercise.name}, ${currentEx.targetValue} repetitions."
                    }
                }
            }
            tts?.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val session = activeSession
    if (session == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("কোনো ব্যায়াম সেশন সক্রিয় নেই")
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when {
            session.isCompleted -> {
                // Congratulations Screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Drawing high-contrast star/trophy bursts on Canvas
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val radius = size.minDimension / 2
                            drawCircle(
                                color = AthleticGreen.copy(alpha = 0.1f),
                                radius = radius
                            )
                            drawCircle(
                                color = AthleticGreen,
                                radius = radius - 20,
                                style = Stroke(width = 6f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = AthleticGreen,
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "দারুণ কাজ! সম্পন্ন হয়েছে!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = AthleticGreen,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "WORKOUT COMPLETED!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Performance Stats Grid Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .frostedGlassCard(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val estDurationSecs = session.exercises.sumOf { if (it.exercise.isDurationBased) it.targetValue else 15 }
                                Text(
                                    text = String.format("%02d:%02d", estDurationSecs / 60, estDurationSecs % 60),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AthleticBlue
                                )
                                Text("সময়কাল (Duration)", fontSize = 11.sp, color = DimSlateText)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val estCalories = session.exercises.sumOf { we ->
                                    val durationMins = (if (we.exercise.isDurationBased) we.targetValue else 15).toFloat() / 60f
                                    (durationMins * we.exercise.caloriesPerMinute).toInt()
                                }.coerceAtLeast(10)
                                Text(
                                    text = "$estCalories KCAL",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AthleticCoral
                                )
                                Text("ক্যালোরি বার্ন", fontSize = 11.sp, color = DimSlateText)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = {
                            viewModel.completeAndSaveWorkout()
                            onWorkoutFinished()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AthleticGreen)
                    ) {
                        Text(
                            text = "ব্যায়াম সম্পন্ন করুন (Complete)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            session.isResting -> {
                // Rest Countdown Screen
                val currentEx = session.exercises[session.currentExerciseIndex]
                val nextEx = session.exercises.getOrNull(session.currentExerciseIndex + 1)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "বিশ্রাম (REST)",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = AthleticGreen,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Take a breath and recover.",
                            fontSize = 14.sp,
                            color = DimSlateText
                        )
                    }

                    // Large rest timer circle
                    Box(
                        modifier = Modifier.size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = AthleticGreen.copy(alpha = 0.15f),
                                radius = size.minDimension / 2
                            )
                            drawCircle(
                                color = AthleticGreen,
                                radius = size.minDimension / 2,
                                style = Stroke(width = 8f)
                            )
                        }

                        Text(
                            text = "${session.secondsRemaining}",
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Rest extra controls
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.restExtraSeconds(20) },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+20s", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.skipCurrentExercise() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("ব্যায়াম শুরু করুন", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Next exercise preview card
                    if (nextEx != null) {
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
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(AthleticBlue.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = AthleticBlue)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text("পরবর্তী ব্যায়াম (UP NEXT)", fontSize = 11.sp, color = DimSlateText, fontWeight = FontWeight.Bold)
                                    Text(nextEx.exercise.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = if (nextEx.exercise.isDurationBased) "${nextEx.targetValue} সেকেন্ড" else "${nextEx.targetValue} বার",
                                        fontSize = 12.sp,
                                        color = AthleticBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }

            else -> {
                // Active Exercise Screen
                val currentEx = session.exercises[session.currentExerciseIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "EXERCISE ${session.currentExerciseIndex + 1} OF ${session.exercises.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AthleticBlue,
                            letterSpacing = 1.sp
                        )

                        TextButton(onClick = { viewModel.cancelWorkout() }) {
                            Text("QUIT", color = AthleticCoral, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Animated stick-man figure canvas
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 12.dp)
                            .frostedGlassCard(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ExerciseAnimation(
                                animationId = currentEx.exercise.animationId,
                                isPaused = session.isPaused,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Description and Instructions
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentEx.exercise.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentEx.exercise.descriptionBn,
                            fontSize = 14.sp,
                            color = DimSlateText,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Big Timer or Rep Count Visualizer
                    if (currentEx.exercise.isDurationBased) {
                        Box(
                            modifier = Modifier.size(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = AthleticCoral.copy(alpha = 0.15f),
                                    radius = size.minDimension / 2
                                )
                                drawCircle(
                                    color = AthleticCoral,
                                    radius = size.minDimension / 2,
                                    style = Stroke(width = 6f)
                                )
                            }
                            Text(
                                text = "${session.secondsRemaining}s",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Black,
                                color = AthleticCoral
                            )
                        }
                    } else {
                        // Rep-based self paced DONE button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "x${currentEx.targetValue}",
                                fontSize = 54.sp,
                                fontWeight = FontWeight.Black,
                                color = AthleticBlue
                            )
                            Text(
                                text = "REPETITIONS",
                                fontSize = 11.sp,
                                color = DimSlateText,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Buttons Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.togglePauseResume() },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(
                                imageVector = if (session.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = "Play/Pause",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Done/Skip primary action button
                        Button(
                            onClick = { viewModel.skipCurrentExercise() },
                            modifier = Modifier
                                .height(56.dp)
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentEx.exercise.isDurationBased) MaterialTheme.colorScheme.primary
                                else AthleticGreen
                            )
                        ) {
                            Text(
                                text = if (currentEx.exercise.isDurationBased) "SKIP" else "DONE (সম্পন্ন)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
