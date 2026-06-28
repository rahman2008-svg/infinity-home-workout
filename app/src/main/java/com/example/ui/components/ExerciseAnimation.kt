package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExerciseAnimation(
    animationId: String,
    modifier: Modifier = Modifier,
    isPaused: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ExerciseAnimation")

    // General progress value oscillating between 0f and 1f for rhythmic movements
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LimbMovement"
    )

    // Secondary rotative progress (0f to 360f) for circles
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotativeMovement"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2
            val scale = minOf(width, height) / 300f

            // Dynamic progress that freezes if paused
            val progress = if (isPaused) 0.5f else animationProgress
            val rotation = if (isPaused) 0f else rotationAngle

            // Let's draw based on the animationId
            when (animationId) {
                "jumping_jack" -> {
                    // Draw ground
                    drawLine(Color.Gray.copy(alpha = 0.3f), Offset(centerX - 100 * scale, centerY + 80 * scale), Offset(centerX + 100 * scale, centerY + 80 * scale), strokeWidth = 4f)

                    // Head
                    val headY = centerY - 60 * scale
                    drawCircle(primaryColor, radius = 15 * scale, center = Offset(centerX, headY))

                    // Torso
                    val hipY = centerY + 10 * scale
                    drawLine(onSurfaceColor, Offset(centerX, headY + 15 * scale), Offset(centerX, hipY), strokeWidth = 8 * scale, cap = StrokeCap.Round)

                    // Arms (expanding/contracting with progress)
                    val armSweep = progress * 1.5f - 0.5f // -0.5 to 1.0
                    val armLength = 40 * scale
                    val leftHandX = centerX - armLength * cos(armSweep)
                    val leftHandY = centerY - 15 * scale - armLength * sin(armSweep)
                    val rightHandX = centerX + armLength * cos(armSweep)
                    val rightHandY = centerY - 15 * scale - armLength * sin(armSweep)

                    drawLine(secondaryColor, Offset(centerX, centerY - 15 * scale), Offset(leftHandX, leftHandY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(secondaryColor, Offset(centerX, centerY - 15 * scale), Offset(rightHandX, rightHandY), strokeWidth = 6 * scale, cap = StrokeCap.Round)

                    // Legs (opening/closing with progress)
                    val legSpread = 15 * scale + (progress * 30 * scale)
                    val footY = centerY + 75 * scale
                    drawLine(tertiaryColor, Offset(centerX, hipY), Offset(centerX - legSpread, footY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(tertiaryColor, Offset(centerX, hipY), Offset(centerX + legSpread, footY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                }

                "push_up" -> {
                    // Ground line
                    val floorY = centerY + 50 * scale
                    drawLine(Color.Gray.copy(alpha = 0.3f), Offset(centerX - 120 * scale, floorY), Offset(centerX + 120 * scale, floorY), strokeWidth = 4f)

                    // Push up animation lowers body closer to the ground
                    val dipOffset = progress * 25 * scale

                    // Torso/Body axis at an incline
                    val footX = centerX - 80 * scale
                    val footY = floorY - 5 * scale
                    val headX = centerX + 60 * scale
                    val headY = floorY - 60 * scale + dipOffset

                    val hipX = footX + (headX - footX) * 0.4f
                    val hipY = footY + (headY - footY) * 0.4f

                    // Head
                    drawCircle(primaryColor, radius = 15 * scale, center = Offset(headX + 10 * scale, headY - 10 * scale))

                    // Body
                    drawLine(onSurfaceColor, Offset(footX, footY), Offset(headX, headY), strokeWidth = 8 * scale, cap = StrokeCap.Round)

                    // Arm / Support
                    val handX = headX - 10 * scale
                    val shoulderX = headX - 20 * scale
                    val shoulderY = headY + 5 * scale

                    // Elbow joint flexes with progress
                    val elbowX = shoulderX - 15 * scale + (progress * 15 * scale)
                    val elbowY = floorY - 20 * scale + (progress * 10 * scale)

                    drawLine(secondaryColor, Offset(shoulderX, shoulderY), Offset(elbowX, elbowY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(secondaryColor, Offset(elbowX, elbowY), Offset(handX, floorY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                }

                "squat" -> {
                    // Ground
                    val floorY = centerY + 80 * scale
                    drawLine(Color.Gray.copy(alpha = 0.3f), Offset(centerX - 80 * scale, floorY), Offset(centerX + 80 * scale, floorY), strokeWidth = 4f)

                    // Lowering body down for squat
                    val squatOffset = progress * 40 * scale

                    // Feet are fixed
                    val footX = centerX - 10 * scale
                    val footY = floorY

                    // Knees bend outwards
                    val kneeX = footX - 30 * scale * (progress)
                    val kneeY = floorY - 40 * scale + squatOffset * 0.5f

                    // Hips move down and back
                    val hipX = footX - 15 * scale * (progress)
                    val hipY = floorY - 75 * scale + squatOffset

                    // Torso tilts slightly forward
                    val headX = hipX + 10 * scale
                    val headY = hipY - 60 * scale

                    // Head
                    drawCircle(primaryColor, radius = 15 * scale, center = Offset(headX, headY))

                    // Torso
                    drawLine(onSurfaceColor, Offset(headX, headY + 15 * scale), Offset(hipX, hipY), strokeWidth = 8 * scale, cap = StrokeCap.Round)

                    // Thighs and Shins
                    drawLine(tertiaryColor, Offset(footX, footY), Offset(kneeX, kneeY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(tertiaryColor, Offset(kneeX, kneeY), Offset(hipX, hipY), strokeWidth = 6 * scale, cap = StrokeCap.Round)

                    // Arms extended forward
                    val handX = headX + 35 * scale
                    val handY = headY + 15 * scale
                    drawLine(secondaryColor, Offset(headX, headY + 25 * scale), Offset(handX, handY), strokeWidth = 5 * scale, cap = StrokeCap.Round)
                }

                "plank" -> {
                    // Ground line
                    val floorY = centerY + 50 * scale
                    drawLine(Color.Gray.copy(alpha = 0.3f), Offset(centerX - 120 * scale, floorY), Offset(centerX + 120 * scale, floorY), strokeWidth = 4f)

                    // Slight shaking effect to simulate plank strain
                    val shakeX = if (isPaused) 0f else (sin(rotation * 10f) * 1.5f * scale)
                    val shakeY = if (isPaused) 0f else (cos(rotation * 8f) * 1.5f * scale)

                    // Plank pose is static but straight
                    val footX = centerX - 85 * scale
                    val footY = floorY - 5 * scale
                    val headX = centerX + 55 * scale + shakeX
                    val headY = floorY - 35 * scale + shakeY

                    // Head
                    drawCircle(primaryColor, radius = 15 * scale, center = Offset(headX + 10 * scale, headY - 10 * scale))

                    // Body
                    drawLine(onSurfaceColor, Offset(footX, footY), Offset(headX, headY), strokeWidth = 8 * scale, cap = StrokeCap.Round)

                    // Elbow Support
                    val elbowX = headX - 20 * scale
                    val elbowY = floorY

                    drawLine(secondaryColor, Offset(headX - 15 * scale, headY + 5 * scale), Offset(elbowX, elbowY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(secondaryColor, Offset(elbowX, elbowY), Offset(headX, floorY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                }

                "high_knees" -> {
                    // Ground
                    val floorY = centerY + 80 * scale
                    drawLine(Color.Gray.copy(alpha = 0.3f), Offset(centerX - 80 * scale, floorY), Offset(centerX + 80 * scale, floorY), strokeWidth = 4f)

                    // Running in place with knees high
                    val headY = centerY - 50 * scale + (sin(rotation * 0.1f) * 4 * scale)
                    drawCircle(primaryColor, radius = 15 * scale, center = Offset(centerX, headY))

                    // Torso
                    val hipY = centerY + 15 * scale
                    drawLine(onSurfaceColor, Offset(centerX, headY + 15 * scale), Offset(centerX, hipY), strokeWidth = 8 * scale, cap = StrokeCap.Round)

                    // High Knees alternate legs
                    val leftKneeHigh = progress > 0.5f
                    val leftKneeY = if (leftKneeHigh) hipY - 20 * scale else floorY - 40 * scale
                    val leftFootY = if (leftKneeHigh) hipY + 10 * scale else floorY

                    val rightKneeHigh = !leftKneeHigh
                    val rightKneeY = if (rightKneeHigh) hipY - 20 * scale else floorY - 40 * scale
                    val rightFootY = if (rightKneeHigh) hipY + 10 * scale else floorY

                    // Left Leg
                    drawLine(tertiaryColor, Offset(centerX, hipY), Offset(centerX - 15 * scale, leftKneeY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(tertiaryColor, Offset(centerX - 15 * scale, leftKneeY), Offset(centerX - 15 * scale, leftFootY), strokeWidth = 6 * scale, cap = StrokeCap.Round)

                    // Right Leg
                    drawLine(secondaryColor, Offset(centerX, hipY), Offset(centerX + 15 * scale, rightKneeY), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(secondaryColor, Offset(centerX + 15 * scale, rightKneeY), Offset(centerX + 15 * scale, rightFootY), strokeWidth = 6 * scale, cap = StrokeCap.Round)

                    // Swinging arms
                    val armOscillation = sin(rotation * 0.1f) * 20 * scale
                    drawLine(onSurfaceColor, Offset(centerX, centerY), Offset(centerX - 25 * scale, centerY + armOscillation), strokeWidth = 5 * scale, cap = StrokeCap.Round)
                    drawLine(onSurfaceColor, Offset(centerX, centerY), Offset(centerX + 25 * scale, centerY - armOscillation), strokeWidth = 5 * scale, cap = StrokeCap.Round)
                }

                "arm_circle" -> {
                    // Body standing
                    drawCircle(primaryColor, radius = 15 * scale, center = Offset(centerX, centerY - 60 * scale))
                    drawLine(onSurfaceColor, Offset(centerX, centerY - 45 * scale), Offset(centerX, centerY + 30 * scale), strokeWidth = 8 * scale, cap = StrokeCap.Round)
                    drawLine(tertiaryColor, Offset(centerX, centerY + 30 * scale), Offset(centerX - 15 * scale, centerY + 100 * scale), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(tertiaryColor, Offset(centerX, centerY + 30 * scale), Offset(centerX + 15 * scale, centerY + 100 * scale), strokeWidth = 6 * scale, cap = StrokeCap.Round)

                    // Circular rotation path of hands
                    val rad = rotation * (Math.PI / 180)
                    val radius = 30 * scale
                    val leftHandX = centerX - 50 * scale + radius * cos(rad).toFloat()
                    val leftHandY = centerY - 15 * scale + radius * sin(rad).toFloat()

                    val rightHandX = centerX + 50 * scale + radius * cos(rad + Math.PI).toFloat()
                    val rightHandY = centerY - 15 * scale + radius * sin(rad + Math.PI).toFloat()

                    // Arm joints
                    drawLine(secondaryColor, Offset(centerX, centerY - 15 * scale), Offset(centerX - 50 * scale, centerY - 15 * scale), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(secondaryColor, Offset(centerX - 50 * scale, centerY - 15 * scale), Offset(leftHandX, leftHandY), strokeWidth = 5 * scale, cap = StrokeCap.Round)

                    drawLine(secondaryColor, Offset(centerX, centerY - 15 * scale), Offset(centerX + 50 * scale, centerY - 15 * scale), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                    drawLine(secondaryColor, Offset(centerX + 50 * scale, centerY - 15 * scale), Offset(rightHandX, rightHandY), strokeWidth = 5 * scale, cap = StrokeCap.Round)

                    // Draw circles dotted guide
                    drawCircle(Color.Gray.copy(alpha = 0.2f), radius = radius, center = Offset(centerX - 50 * scale, centerY - 15 * scale), style = Stroke(width = 2f))
                    drawCircle(Color.Gray.copy(alpha = 0.2f), radius = radius, center = Offset(centerX + 50 * scale, centerY - 15 * scale), style = Stroke(width = 2f))
                }

                "cobra_stretch" -> {
                    // Floor
                    val floorY = centerY + 60 * scale
                    drawLine(Color.Gray.copy(alpha = 0.3f), Offset(centerX - 120 * scale, floorY), Offset(centerX + 120 * scale, floorY), strokeWidth = 4f)

                    // Cobra pose stretches spine backwards
                    val hipX = centerX - 20 * scale
                    val hipY = floorY - 5 * scale

                    val headX = centerX + 50 * scale
                    // Oscillating gentle breathing stretch
                    val headY = floorY - 65 * scale - (progress * 8 * scale)

                    // Head
                    drawCircle(primaryColor, radius = 15 * scale, center = Offset(headX, headY - 15 * scale))

                    // Arching Back
                    drawLine(onSurfaceColor, Offset(centerX - 100 * scale, floorY - 2 * scale), Offset(hipX, hipY), strokeWidth = 8 * scale, cap = StrokeCap.Round)
                    drawLine(onSurfaceColor, Offset(hipX, hipY), Offset(headX, headY), strokeWidth = 8 * scale, cap = StrokeCap.Round)

                    // Hand support pushing up
                    drawLine(secondaryColor, Offset(centerX + 10 * scale, floorY), Offset(centerX + 20 * scale, headY + 15 * scale), strokeWidth = 6 * scale, cap = StrokeCap.Round)
                }

                "child_pose" -> {
                    // Floor
                    val floorY = centerY + 60 * scale
                    drawLine(Color.Gray.copy(alpha = 0.3f), Offset(centerX - 100 * scale, floorY), Offset(centerX + 100 * scale, floorY), strokeWidth = 4f)

                    // Hips on heels, arms stretched out front
                    val heelX = centerX - 60 * scale
                    val headX = centerX + 30 * scale
                    val headY = floorY - 15 * scale - (progress * 4 * scale) // gentle deep breaths

                    // Body arched down
                    drawLine(onSurfaceColor, Offset(heelX, floorY - 10 * scale), Offset(centerX - 10 * scale, floorY - 25 * scale), strokeWidth = 8 * scale, cap = StrokeCap.Round)
                    drawLine(onSurfaceColor, Offset(centerX - 10 * scale, floorY - 25 * scale), Offset(headX, headY), strokeWidth = 8 * scale, cap = StrokeCap.Round)

                    // Head
                    drawCircle(primaryColor, radius = 12 * scale, center = Offset(headX + 10 * scale, headY + 5 * scale))

                    // Arms flat on the floor stretching forward
                    drawLine(secondaryColor, Offset(centerX - 10 * scale, floorY - 20 * scale), Offset(centerX + 70 * scale, floorY - 2 * scale), strokeWidth = 5 * scale, cap = StrokeCap.Round)
                }

                else -> {
                    // Default generic dynamic activity indicator
                    drawCircle(primaryColor.copy(alpha = 0.15f), radius = 80 * scale, center = Offset(centerX, centerY))
                    drawCircle(primaryColor, radius = 15 * scale + progress * 5 * scale, center = Offset(centerX, centerY))

                    // Rotating orbiting dots
                    val rad = (rotation * Math.PI / 180)
                    val x1 = centerX + 50 * scale * cos(rad).toFloat()
                    val y1 = centerY + 50 * scale * sin(rad).toFloat()
                    val x2 = centerX + 50 * scale * cos(rad + Math.PI).toFloat()
                    val y2 = centerY + 50 * scale * sin(rad + Math.PI).toFloat()

                    drawCircle(secondaryColor, radius = 8 * scale, center = Offset(x1, y1))
                    drawCircle(tertiaryColor, radius = 6 * scale, center = Offset(x2, y2))
                }
            }
        }
    }
}
