package com.example.sagetech

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

data class EmojiObject(
    val emoji: String,
    val description: String,
    val feetThreshold: Int
)

@Composable
fun DistanceObjectIcon(
    scrollDistance: Int,
    minSize: Float = 24f,
    maxSize: Float = 64f
) {
    val emojiObjects = listOf(
        EmojiObject("🍌", "Banana", 1),
        EmojiObject("🐈", "Cat", 2),
        EmojiObject("🦵", "Human leg", 4),
        EmojiObject("🧍", "Human height", 6),
        EmojiObject("🐊", "Alligator", 9),
        EmojiObject("🚣", "Kayak", 12),
        EmojiObject("🦒", "Giraffe neck", 15),
        EmojiObject("🚌", "School bus", 20),
        EmojiObject("🦖", "T-Rex", 25),
        EmojiObject("🚃", "Train Car", 30),
        EmojiObject("🌲", "Giant Redwood Tree", 100),
        EmojiObject("📡", "Telephone Poles", 120),
        EmojiObject("🚀", "Saturn V Rocket", 180),
        EmojiObject("🌉", "Bridge Span", 250)
    )

    // Conversion factor: 1 ft = 1152 * 5 pixels (5760 pixels per foot)
    val feetToPixels = 1152 * 5
    val scrollDistanceInFeet = scrollDistance.toFloat() / feetToPixels

    // Find the appropriate emoji for the current scroll distance
    val currentEmoji = emojiObjects.lastOrNull { it.feetThreshold <= scrollDistanceInFeet }
        ?: emojiObjects.first()

    // Calculate the emoji size using the logarithmic formula
    // emoji_size = min_size + (log(real_size) - log(1)) / (log(300) - log(1)) * (max_size - min_size)
    val realSize = max(scrollDistanceInFeet, 1f)
    val maxLogValue = ln(300f)
    val currentLogValue = ln(realSize)
    val scaleFactor = (currentLogValue - 0f) / maxLogValue
    val emojiSize = minSize + scaleFactor * (maxSize - minSize)
    
    // Clamp the emoji size between minSize and maxSize
    val finalSize = min(max(emojiSize, minSize), maxSize)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(finalSize.dp * 1.5f)
    ) {
        Text(
            text = currentEmoji.emoji,
            fontSize = finalSize.sp
        )
    }
}