package com.example.sagetech.ui.screens

import OverlayWindowManager
import android.content.Context
import android.transition.ChangeScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.sagetech.R
import com.example.sagetech.ui.model.User

data class HomePageUiState(
    val username : String = "Shreyash Neeraj",
    val scroll: Int = 0

)