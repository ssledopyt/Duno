package com.example.duno.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(): ViewModel() {
    private val _value = MutableStateFlow("")
    val value: StateFlow<String> = _value.asStateFlow()

    private val _zoomOfMap = MutableStateFlow(12.0f)
    val zoomOfMap: StateFlow<Float> = _zoomOfMap.asStateFlow()

    var createEvent by mutableStateOf(false)

    fun setValue(value: String) {
        _value.value = value
    }

    fun plusZoom() {
        _zoomOfMap.value = _zoomOfMap.value+1f
    }

    fun minusZoom() {
        _zoomOfMap.value = _zoomOfMap.value-1f
    }
}