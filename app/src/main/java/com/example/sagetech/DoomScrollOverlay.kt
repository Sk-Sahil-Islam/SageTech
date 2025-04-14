package com.example.sagetech

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun DoomScrollOverlay(scrollDistance: Int) {
    // Convert scroll distance to a fun measurement.
    val measurement = formatScrollDistance(scrollDistance)

    // Animate the overlay's transparency based on scroll distance.
    val overlayAlpha by animateFloatAsState(
        targetValue = (scrollDistance.coerceAtMost(1000).toFloat() / 1000f)
    )

    // Display a simple overlay that shows the measurement.
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = overlayAlpha))
    ) {
        Text(
            text = "You've scrolled: $measurement",
            color = Color.Red,
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.7f))
                .padding(16.dp)
        )

//        Button(onClick = {}) { Text("hey") }
    }
}

// Utility function to format scroll distance into playful real-world units.
fun formatScrollDistance(scrollPixels: Int): String {
    val humanLengthPx = 500     // 500 pixels is one "human" length
    val giraffeLengthPx = 1200  // 1200 pixels is one "giraffe" length
    val bananaLengthPx = 50     // 50 pixels is one "banana" length

    return when {
        scrollPixels >= giraffeLengthPx -> {
            val count = scrollPixels.toFloat() / giraffeLengthPx
            "${count.roundToInt()} giraffe${if (count >= 2) "s" else ""} long"
        }
        scrollPixels >= humanLengthPx -> {
            val count = scrollPixels.toFloat() / humanLengthPx
            "${count.roundToInt()} human${if (count >= 2) "s" else ""} long"
        }
        else -> {
            val count = scrollPixels.toFloat() / bananaLengthPx
            "${count.roundToInt()} banana${if (count >= 2) "s" else ""} long"
        }
    }
}