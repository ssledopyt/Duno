package com.example.duno.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.duno.db.ApiLocationOfSP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(): ViewModel() {
    private val _value = MutableStateFlow("")
    val value: StateFlow<String> = _value.asStateFlow()


    private val _openPlaceData = MutableLiveData<ApiLocationOfSP>()
    val openPlaceData: LiveData<ApiLocationOfSP>
        get() = _openPlaceData

    var createEvent by mutableStateOf(false)

    var openPlace by mutableStateOf(false)

    fun setValue(value: ApiLocationOfSP?) {

        _openPlaceData.value = value
        openPlace = true
    }
    fun setValueOP(value: Boolean) {
        openPlace = value
    }

}