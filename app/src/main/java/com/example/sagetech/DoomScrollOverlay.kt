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




fun formatScrollDistance(scrollPixels: Int): String {
    // Conversion factor: 1 ft = 1152 * 5 pixels (5760 pixels per foot)
    val feetToPixels = 1152 * 5

    // Object list: Pair(Value, Object Name)
    val objects = listOf(
        1 to "Banana",
        2 to "Cat",
        4 to "Human leg",
        6 to "Human height",
        9 to "Alligator",
        12 to "Kayak",
        15 to "Giraffe neck",
        20 to "School bus",
        25 to "T-Rex",
        30 to "Train Car (1 bogie)",
        35 to "London Double-Decker Bus",
        40 to "Shipping Container",
        50 to "Semi Truck Trailer",
        60 to "Humpback Whale",
        70 to "Boeing 737 Wing",
        80 to "Brontosaurus (est.)",
        90 to "Baseball Diamond Baseline",
        100 to "Giant Redwood Tree (small)",
        120 to "Telephone Pole Stack (3x)",
        150 to "Blue Whale (full length)",
        180 to "Apollo Saturn V Rocket",
        200 to "Statue of Liberty (to torch)",
        250 to "Suspension Bridge Span",
        275 to "Football Field Sideline",
        300 to "The Eiffel Tower Base Width"
    )

    // Demotivating, short action phrases (1-3 words)
    val actions = listOf(
        "Wasted time scrolling",
        "Nothing gained scrolling about",
        "Empty scrolled,",
        "Hopelessly swiped",
        "Pointless move scrolled,"
    )

    // Additional descriptors for variety.
    val measurements = listOf(
        "tall", "long", "end-to-end",
        "stacked", "laid out", "in a row"
    )

    // Convert objects list into thresholds (in pixels) paired with their names.
    val objectThresholds = objects.map { (value, name) -> value * feetToPixels to name }

    // Find the appropriate object for the given scrollPixels.
    val result = objectThresholds.lastOrNull { (threshold, _) -> scrollPixels >= threshold }

    return if (result != null) {
        val (threshold, objectName) = result
        val count = (scrollPixels.toFloat() / threshold).roundToInt()
        val displayName = if (count > 1) "${objectName}s" else objectName

        // Unique key for deterministic selection.
        val optionKey = "$threshold-$objectName-$count"
        val actionIndex = optionKey.hashCode().absoluteValue % actions.size
        val chosenAction = actions[actionIndex]
        val measurementKey = "$optionKey-measurement"
        val measurementIndex = measurementKey.hashCode().absoluteValue % measurements.size
        val chosenMeasurement = measurements[measurementIndex]

        // Note the inserted space after chosenAction to ensure smooth concatenation.
        "$chosenAction ${if (count > 1) "$count" else "One"} $displayName $chosenMeasurement!"
    } else {
        // Fallback: if scrollPixels is too small, convert pixels to feet.
        val feet = (scrollPixels.toFloat() / feetToPixels).roundToInt()
        val optionKey = "fallback-$feet"
        val actionIndex = optionKey.hashCode().absoluteValue % actions.size
        val chosenAction = actions[actionIndex]
        "$chosenAction ${feet} ft"
    }
}

data class EmojiObject(
    val emojiId: String,
    val feet: Int
)