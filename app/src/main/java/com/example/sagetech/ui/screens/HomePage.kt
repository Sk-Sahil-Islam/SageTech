package com.example.sagetech.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sagetech.R
import com.example.sagetech.ui.model.User
import com.example.sagetech.ui.model.bullyList
import kotlin.random.Random

// Load saved scroll distance
fun getSavedScrollDistance(context: Context, prefName: String, distanceKey: String): Int {
    val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    return prefs.getInt(distanceKey, 0)
}

@Composable
fun HomeScreen(
    viewModel: HomePageViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val PREFS_NAME = "doom_scroll_prefs"
    val KEY_SCROLL_DISTANCE = "total_scroll_distance"

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
    ) {innerPadding ->

        val context = LocalContext.current

        LazyColumn(
            Modifier
                .padding(innerPadding)
        ) {
            val value = getSavedScrollDistance(context, PREFS_NAME, KEY_SCROLL_DISTANCE)
            Log.e("HomePage", value.toString())
            item {
                Text(
                    text = "You have Scrolled about ${value} ft! ",
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
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    conversions.forEach { (emoji, label, unitFt) ->
                        val count = state.scroll / unitFt
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

                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
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
}




@Preview
@Composable
fun PreviewHomeScreen(){
    HomeScreen()
}