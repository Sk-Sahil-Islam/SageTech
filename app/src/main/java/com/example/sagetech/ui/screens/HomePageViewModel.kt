package com.example.sagetech.ui.screens

import OverlayWindowManager
import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomePageViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomePageUiState())
    val state = _state.asStateFlow()

    fun changeScroll(context: Context){
        _state.update {
            it.copy(scroll = OverlayWindowManager.getSavedScrollDistance(context))
        }
    }


}