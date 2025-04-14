package com.example.sagetech.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sagetech.R
import com.example.sagetech.ui.model.User

@Composable
fun LeaderboardScreen() {
    val leaderboardList = listOf(
        User(username = "Shreyash", rank = 1, scroll = 98L, pfp = painterResource(R.drawable.user_pfp)),
        User(username = "Aanya", rank = 2, scroll = 92L, pfp = painterResource(R.drawable.user_pfp)),
        User(username = "Vikram", rank = 3, scroll = 87L, pfp = painterResource(R.drawable.user_pfp)),
        User(username = "Anika", rank = 4, scroll = 75L, pfp = painterResource(R.drawable.user_pfp)),
        User(username = "Kabir", rank = 5, scroll = 60L, pfp = painterResource(R.drawable.user_pfp)),
        User(username = "Tara", rank = 6, scroll = 58L, pfp = painterResource(R.drawable.user_pfp))
    )

    Scaffold(

    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                leaderboardList.forEach(){ user ->
                    LeaderboardItem(
                        rank = user.rank,
                        name = user.username,
                        scrollCount = user.scroll,
                        profileRes = user.pfp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true) @Composable
fun PreviewLeaderboard(){
    LeaderboardScreen()
}

@Composable
fun LeaderboardItem(
    rank: Int,
    name: String,
    scrollCount: Long,
    profileRes: Painter,
    modifier: Modifier = Modifier
) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Gray
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Rank
            Text(
                text = "#$rank",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = rankColor
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Profile Picture
            Image(
                painter = profileRes,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Name and Scroll info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$scrollCount bananas scrolled 🍌",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Trophy Icon (optional)
            if (rank <= 3) {
                Image(
                    painter = painterResource(
                        id = when (rank) {
                            1 -> R.drawable.gold
                            2 -> R.drawable.silver
                            3 -> R.drawable.bronze
                            else -> R.drawable.bronze
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

