package com.example.duno.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MeetingViewModel : ViewModel() {
    private val _meetingList = MutableLiveData(listOf<Meeting>())
    val meetingList: LiveData<List<Meeting>> = _meetingList

    fun updateMeetingList(newMeetings: List<Meeting>) {
        _meetingList.value = _meetingList.value?.plus(newMeetings)
    }
}


// Функция обновления списка
fun updateList(viewModel: MeetingViewModel, newMeetings: List<Meeting>) {
    viewModel.updateMeetingList(newMeetings)
}