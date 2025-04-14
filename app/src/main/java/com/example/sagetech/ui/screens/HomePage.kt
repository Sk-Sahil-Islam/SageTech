package com.example.sagetech.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sagetech.R
import com.example.sagetech.ui.model.bullyList
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// Load saved scroll distance
fun getSavedScrollDistance(context: Context, prefName: String, distanceKey: String): Int {
    val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    return prefs.getInt(distanceKey, 0)
}

val feetToPixels = 1152 * 5  // 5760 pixels per foot

@Composable
fun HomeScreen() {
    val PREFS_NAME = "doom_scroll_prefs"
    val KEY_SCROLL_DISTANCE = "total_scroll_distance"
    val context = LocalContext.current
    val feetToPixels = 1152 * 5  // Same conversion factor as in formatScrollDistance

    // Use remember in combination with LaunchedEffect to load the value once and update it when needed
    var savedScrollDistance by remember { mutableStateOf(0) }

    // Create a refresh trigger that updates every second
    val refreshTrigger by produceState(initialValue = 0) {
        while (true) {
            delay(1000) // Update every second
            value = (value + 1) % 1000 // Just a changing value to trigger recomposition
        }
    }

    // Update the saved scroll distance whenever the refresh trigger changes
    LaunchedEffect(refreshTrigger) {
        savedScrollDistance = getSavedScrollDistance(context, PREFS_NAME, KEY_SCROLL_DISTANCE)
    }

    // Convert pixels to feet
    val feet = (savedScrollDistance.toFloat() / feetToPixels).roundToInt()

    Scaffold(
        topBar = {
            ElevatedCard(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.user_pfp),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(15.dp))
                        Text(
                            text = "Shreyash Neeraj",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Image(
                        painter = painterResource(R.drawable.podium),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding)
        ) {
            item {
                Text(
                    text = "You have Scrolled about ${feet} ft! ",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(28.dp)
                )
            }

            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Text(
                        text = " '${bullyList.get(2)}'",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            item {
                Spacer(Modifier.height(30.dp))
                Text(
                    text = "📊 Now That's about",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 12.dp),
                    color = Color(0xFF222831)
                )

                val conversions = listOf(
                    Triple("🍌", "Bananas", 1),
                    Triple("🦒", "Giraffes", 20),
                    Triple("\uD83D\uDC0B", "Blue Whale", 100),
                    Triple("🗼", "Eiffel Towers", 300)
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    conversions.forEach { (emoji, label, unitFt) ->
                        val count = feet / unitFt
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = emoji,
                                        fontSize = 28.sp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = label,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF222831)
                                        )
                                    }
                                }

                                Text(
                                    text = count.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF00ADB5)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun PreviewHomeScreen(){
    HomeScreen()
}