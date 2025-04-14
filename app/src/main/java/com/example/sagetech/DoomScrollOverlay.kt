package com.example.sagetech

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun DoomScrollOverlay(
    scrollDistance: Int
) {
    // Convert scroll distance to a fun measurement.
    val measurement = formatScrollDistance(scrollDistance)

    // Animate the overlay's transparency based on scroll distance.
    val overlayAlpha by animateFloatAsState(
        targetValue = (scrollDistance.coerceAtMost(1000).toFloat() / 1000f)
    )

    // Display a simple overlay that shows the measurement.
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .background(Color.Black.copy(alpha = overlayAlpha))
//            .padding(16.dp)

    ) {
        Text(
            text = measurement,
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Center)

//                .padding(16.dp)
        )
    }
}



data class ScrollMilestone(
    val thresholdInFeet: Int,
    val message: String
)

fun formatScrollDistance(scrollPixels: Int): String {
    // Conversion factor: 1 ft = 1152 * 5 pixels (5760 pixels per foot)
    val feetToPixels = 1152 * 5

    val scrollMilestones = listOf(
        ScrollMilestone(1, "One banana down. Proud of yourself?🍌"),
        ScrollMilestone(2, "A cat's worth already? Even whiskers are judging you.🐈"),
        ScrollMilestone(4, "You've scrolled a whole leg. Yours are probably asleep.🦵"),
        ScrollMilestone(6, "Congratulations. You've out-scrolled a human life.🧍"),
        ScrollMilestone(9, "That's a full alligator of doomscrolling 🐊. Snap out of it."),
        ScrollMilestone(12, "You're literally paddling through nonsense. 🚣‍♂ Is it worth it?"),
        ScrollMilestone(15, "Giraffe-high scrolls detected. 🦒 Perspective check: what are you even looking for?"),
        ScrollMilestone(20, "Bus full of scrolls. 🚌 All headed nowhere fast."),
        ScrollMilestone(25, "Jurassic-level scrolling. 🦖 Fossils are impressed, but why are you still here?"),
        ScrollMilestone(30, "You just filled a train car with useless content. 🚃 All aboard the regret express."),
        ScrollMilestone(100, "Redwood scroll height achieved. 🌲 That's not enlightenment, that's avoidance."),
        ScrollMilestone(120, "Three poles of scroll. 📶 Your signal's strong, but your willpower? Not so much."),
        ScrollMilestone(180, "You've launched into scroll orbit. Come back to Earth, please.🚀"),
        ScrollMilestone(250, "You've scrolled across a bridge 🌉  sadly, not out of your habits")
    )

    // Convert thresholds to pixels
    val pixelMilestones = scrollMilestones.map { 
        ScrollMilestone(it.thresholdInFeet * feetToPixels, it.message) 
    }

    // Find the appropriate message for the given scrollPixels
    val result = pixelMilestones.lastOrNull { it.thresholdInFeet <= scrollPixels }

    return result?.message ?: "You've started scrolling..." // Fallback message
}

data class EmojiObject(
    val emojiId: String,
    val feet: Int
)