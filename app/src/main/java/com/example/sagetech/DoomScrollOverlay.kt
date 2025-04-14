package com.example.sagetech

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

enum class DoomScrollMode {
    TEXT,
    EMOJI
}

// Update the DoomScrollOverlay composable to use the rotation
@Composable
fun DoomScrollOverlay(
    scrollDistance: Int,
    mode: DoomScrollMode = DoomScrollMode.TEXT,
    maxScrollFeet: Int = 20
) {
    when (mode) {
        DoomScrollMode.TEXT -> {
            // Text mode - show the milestone message
            val measurement = formatScrollDistance(scrollDistance)

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = measurement,
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        DoomScrollMode.EMOJI -> {
            // Get emoji, size fraction, and rotation angle
            val (emoji, sizeFraction, rotationAngle) = getEmojiForScroll(scrollDistance, maxScrollFeet)

            // Remember the last emoji to detect changes
            val lastEmoji = remember { mutableStateOf(emoji) }

            // Remember the rotation angle
            var currentRotation by remember { mutableStateOf(rotationAngle) }

            // Update rotation when emoji changes
            if (emoji != lastEmoji.value) {
                lastEmoji.value = emoji
                currentRotation = rotationAngle
            }

            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val maxEmojiSize = screenHeight * 0.75f // 75% of screen height
            val emojiSize = maxEmojiSize * sizeFraction

            Box(
                modifier = Modifier
                    .size((emojiSize))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(0.75f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(emoji),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
//                        .size(emojiSize)
                        .rotate(currentRotation)
                        .padding(16.dp)
                )
            }
        }
    }
}

// Modify getEmojiForScroll to include random rotation
fun getEmojiForScroll(scrollDistance: Int, maxScrollFeet: Int): Triple<Int, Float, Float> {
    val feetToPixels = 1152 * 4
    val scrollFeet = (scrollDistance.toFloat() / feetToPixels).coerceAtMost(maxScrollFeet.toFloat())
    val sizeFraction = (scrollFeet / maxScrollFeet).coerceIn(0.1f, 1.0f)

    // Define emoji progression based on feet
    val emojiObjects = listOf(
        EmojiObject(R.drawable.banana, 1),      // Banana
        EmojiObject(R.drawable.kitty, 2),      // Cat
        EmojiObject(R.drawable.leg, 4),      // Human leg
        EmojiObject(R.drawable.man, 6),      // Human
        EmojiObject(R.drawable.app, 9),      // Alligator
        EmojiObject(R.drawable.boat, 12),     // Kayak
        EmojiObject(R.drawable.giraffe, 15),     // Giraffe
        EmojiObject(R.drawable.bus, 20),     // School bus
        EmojiObject(R.drawable.rex, 25),     // T-Rex
        EmojiObject(R.drawable.transport, 30),     // Train
        EmojiObject(R.drawable.tree, 100),    // Redwood Tree
//        EmojiObject("📱", 120),    // Phone/Pole
//        EmojiObject("🚀", 180),    // Rocket
//        EmojiObject("🌉", 250),    // Bridge
//        EmojiObject("🌍", 500),    // Earth
//        EmojiObject("🌌", 1000)    // Galaxy
    )

    val selectedEmoji = emojiObjects.lastOrNull { it.feet <= scrollFeet } ?: emojiObjects.first()

    // Generate a random angle between -45 and 45 degrees
    val randomAngle = Random.nextFloat() * 90f - 45f

    return Triple(selectedEmoji.emojiId, sizeFraction, randomAngle)
}

// Original text formatting function remains unchanged
fun formatScrollDistance(scrollPixels: Int): String {
    // Conversion factor: 1 ft = 1152 * 4 pixels (4608 pixels per foot)
    val feetToPixels = 1152 * 4

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

data class ScrollMilestone(
    val thresholdInFeet: Int,
    val message: String
)

// Add a rotation angle to the EmojiObject class
data class EmojiObject(
    val emojiId: Int,
    val feet: Int,
    val rotationAngle: Float = 0f // Default angle is 0
)