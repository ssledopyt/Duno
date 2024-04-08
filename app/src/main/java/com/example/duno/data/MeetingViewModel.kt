package com.example.duno.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.db.ApiMeeting
import com.example.duno.db.ApiRepository
import com.example.duno.db.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(private val repository: MeetingRepository):ViewModel(){
    private val _meetingList = MutableLiveData(DunoEventUIState(loading = true))
    val meetingList : LiveData<DunoEventUIState>
        get() = _meetingList

    init {
        getAllMeetings()
    }

    private fun getAllMeetings() = viewModelScope.launch {
        repository.getAllMeetings()
            .catch {
                _meetingList.value = DunoEventUIState(error = it.message)
            }.collect{
                _meetingList.value= DunoEventUIState(
                    events = if (it.data.isNullOrEmpty()) emptyList() else it.data,
                    loading = false
                )
            }

    }

/*    fun getMeeting(meetingID: Int) = viewModelScope.launch {
        repository.getMeeting(meetingID)
            .catch {ex ->

            }
            .collect{
                _meetingList.value.data.find=it
            }
    }*/
}

data class DunoEventUIState(
    val events: List<ApiMeeting>? = emptyList(),
    val selectedEmails: Set<Int> = emptySet(),
    val openedEmail: ApiMeeting? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)