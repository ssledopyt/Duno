package com.example.duno.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.db.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(private val repository: MeetingRepository):ViewModel(){
    private val _meetingList = MutableLiveData(listOf<Meeting>())
    val meetingList: LiveData<List<Meeting>> = _meetingList

    init {
        getAllMeetings()
    }

    private fun getAllMeetings(){
        viewModelScope.launch {

        }
    }

    fun updateMeetingList(newMeetings: List<Meeting>) {
        _meetingList.value = _meetingList.value?.plus(newMeetings)
    }
}


// Функция обновления списка
fun updateList(viewModel: MeetingViewModel, newMeetings: List<Meeting>) {
    viewModel.updateMeetingList(newMeetings)
}
