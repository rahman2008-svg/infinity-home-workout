package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AthleticBlue
import com.example.ui.theme.AthleticCoral
import com.example.ui.theme.AthleticGreen
import com.example.ui.theme.frostedGlassCard
import com.example.ui.theme.GlassCardBg
import com.example.ui.theme.GlassCardBorder

@Composable
fun OnboardingScreen(
    onOnboardingComplete: (
        name: String,
        age: Int,
        height: Float,
        currentWeight: Float,
        targetWeight: Float,
        goal: String,
        fitnessLevel: String,
        lowImpactMode: Boolean
    ) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    // User inputs
    var name by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Lose Weight") }
    var age by remember { mutableStateOf("25") }
    var height by remember { mutableStateOf("170") }
    var currentWeight by remember { mutableStateOf("70") }
    var targetWeight by remember { mutableStateOf("65") }
    var fitnessLevel by remember { mutableStateOf("Beginner") }
    var lowImpactMode by remember { mutableStateOf(false) }

    val totalSteps = 4
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val goals = listOf(
        "Lose Weight" to "ওজন কমাতে চান",
        "Lose Belly Fat" to "পেটের মেদ কমাতে চান",
        "Chest Fat Burn" to "বুকের চর্বি কমাতে চান",
        "Six Pack Abs" to "সিক্স প্যাক বানাতে চান",
        "Keep Full Body Fit" to "পুরো শরীর ফিট রাখতে চান"
    )

    val fitnessLevels = listOf(
        "Beginner" to "ব্যায়ামের কোনো পূর্ব অভিজ্ঞতা নেই (সুপারিশকৃত)",
        "Intermediate" to "মাঝেমধ্যে ব্যায়াম করেন এবং কিছুটা ফিট",
        "Advanced" to "নিয়মিত ভারী ব্যায়াম করতে অভ্যস্ত"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Gradient overlay for background depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            Color.Transparent,
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Logo and Progress bar
            if (step > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step $step of $totalSteps",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.clickable { step = totalSteps }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { (step - 1).toFloat() / (totalSteps - 1).toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Step Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (step) {
                    1 -> {
                        // Welcome Screen
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Gym Icon",
                                tint = AthleticCoral,
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "INFINITY",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = AthleticBlue,
                                letterSpacing = 4.sp
                            )
                            Text(
                                text = "HOME WORKOUT",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "কোনো জিম বা যন্ত্রপাতির প্রয়োজন নেই। বাড়িতে বসেই মাত্র ৫–১০ মিনিটে মেদ ঝরিয়ে আকর্ষণীয় ফিগার গড়ে তুলুন!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                lineHeight = 24.sp
                            )
                        }
                    }

                    2 -> {
                        // Goal Selection
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "আপনার প্রধান লক্ষ্যটি কী?",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Choose your main weight loss or muscle building goal.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            goals.forEach { (engName, bngName) ->
                                val isSelected = goal == engName
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .frostedGlassCard()
                                        .clickable { goal = engName },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        else Color.Transparent
                                    ),
                                    border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = bngName,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                            Text(
                                                text = engName,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        // User Stats Capture
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "আপনার শারীরিক তথ্য প্রদান করুন",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "This helps us calculate your BMI and customize the workout strength.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("আপনার নাম (Name)") },
                                placeholder = { Text("যেমন: আবদুর রহমান") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = age,
                                    onValueChange = { age = it },
                                    label = { Text("বয়স (Age)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                OutlinedTextField(
                                    value = height,
                                    onValueChange = { height = it },
                                    label = { Text("উচ্চতা (Height cm)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = currentWeight,
                                    onValueChange = { currentWeight = it },
                                    label = { Text("বর্তমান ওজন (Weight kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                OutlinedTextField(
                                    value = targetWeight,
                                    onValueChange = { targetWeight = it },
                                    label = { Text("লক্ষ্যমাত্রা ওজন (Target kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Low Impact Mode toggle card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .frostedGlassCard()
                                    .clickable { lowImpactMode = !lowImpactMode },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (lowImpactMode) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                    else Color.Transparent
                                ),
                                border = if (lowImpactMode) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
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
                                            text = "Low-Impact Mode (সহজ মোড)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "যাদের হাঁটুতে ব্যথা বা জয়েন্টের সমস্যা আছে তাদের জন্য নিরাপদ ও সহজ ব্যায়াম রুটিন তৈরি করা হবে।",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                        )
                                    }
                                    Switch(
                                        checked = lowImpactMode,
                                        onCheckedChange = { lowImpactMode = it }
                                    )
                                }
                            }
                        }
                    }

                    4 -> {
                        // Difficulty selection
                        Column(
                            modifier = Modifier
                                        .fillMaxWidth()
                                        .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "আপনার ফিটনেস লেভেল কত?",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Select a difficulty that matches your physical capabilities.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            fitnessLevels.forEach { (level, desc) ->
                                val isSelected = fitnessLevel == level
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .frostedGlassCard()
                                        .clickable { fitnessLevel = level },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        else Color.Transparent
                                    ),
                                    border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (level == "Beginner") "Beginner (নতুন)" else if (level == "Intermediate") "Intermediate (মধ্যম)" else "Advanced (উন্নত)",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = desc,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                                lineHeight = 18.sp
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Custom calculated recommendation note based on details
                            val parsedWeight = currentWeight.toFloatOrNull() ?: 70f
                            val parsedHeight = (height.toFloatOrNull() ?: 170f) / 100f
                            val bmi = parsedWeight / (parsedHeight * parsedHeight)
                            val bmiResult = when {
                                bmi < 18.5f -> "Underweight (কম ওজন)"
                                bmi < 25.0f -> "Normal (স্বাভাবিক)"
                                bmi < 30.0f -> "Overweight (অতিরিক্ত ওজন)"
                                else -> "Obese (স্থূলতা)"
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .frostedGlassCard(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "স্বাস্থ্য মূল্যায়ন ও সুপারিশ (BMI: ${String.format("%.1f", bmi)} - $bmiResult)",
                                        fontWeight = FontWeight.Bold,
                                        color = AthleticGreen,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = when {
                                            bmi >= 25f -> "আপনার বিএমআই অনুযায়ী সহজ বা নতুন (Beginner) মোডটি সবচেয়ে নিরাপদ হবে, যা জয়েন্টে চাপ কমাবে।"
                                            else -> "আপনার জন্য আমরা 'Beginner' থেকে শুরু করে ধীরে ধীরে শরীরকে ফিট করার সুপারিশ করছি।"
                                        },
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 1) {
                    TextButton(onClick = { step-- }) {
                        Text("Back", fontSize = 16.sp)
                    }
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }

                Button(
                    onClick = {
                        keyboardController?.hide()
                        if (step < totalSteps) {
                            step++
                        } else {
                            // Complete and save onboarding
                            val profileName = name.ifEmpty { "User" }
                            val profileAge = age.toIntOrNull() ?: 25
                            val profileHeight = height.toFloatOrNull() ?: 170f
                            val profileWeight = currentWeight.toFloatOrNull() ?: 70f
                            val profileTarget = targetWeight.toFloatOrNull() ?: 65f
                            onOnboardingComplete(
                                profileName,
                                profileAge,
                                profileHeight,
                                profileWeight,
                                profileTarget,
                                goal,
                                fitnessLevel,
                                lowImpactMode
                            )
                        }
                    },
                    modifier = Modifier
                        .height(54.dp)
                        .weight(1f)
                        .padding(start = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (step == 1) "Get Started" else if (step == totalSteps) "Build My Plan!" else "Next",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Icon"
                        )
                    }
                }
            }
        }
    }
}

// Utility extension
private fun Modifier.border(width: androidx.compose.ui.unit.Dp, color: Color): Modifier =
    this.border(width, color, RoundedCornerShape(12.dp))
