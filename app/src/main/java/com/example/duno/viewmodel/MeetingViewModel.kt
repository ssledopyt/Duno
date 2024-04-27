package com.example.duno.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.data.MeetingRepository
import com.example.duno.db.ApiLikes
import com.example.duno.db.ApiMeeting
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

    fun getAllMeetings() = viewModelScope.launch {
        Timber.tag("MeetingViewModel").e("get all meetings")
        repository.getAllMeetings()
            .catch {
                _meetingList.value = DunoEventUIState(error = it.message)
            }.collect{
                Timber.tag("MeetingViewModel").e(it.data.toString())
                _meetingList.value= DunoEventUIState(
                    events = if (it.data.isNullOrEmpty()) emptyList() else it.data,
                )
            }

    }

    fun getUserLikes(nickname:String) = viewModelScope.launch {
        repository.getUserLikes(nickname).catch {
            _meetingList.value = DunoEventUIState(
                events = meetingList.value!!.events,
                favEvents = meetingList.value!!.favEvents,
                openedEvent = meetingList.value!!.openedEvent,
                isDetailOnlyOpen = meetingList.value!!.isDetailOnlyOpen,
                loading = meetingList.value!!.loading,
                error = it.message
            )
        }.collect{
            _meetingList.value = DunoEventUIState(
                events = meetingList.value!!.events,
                favEvents = it.data,
                openedEvent = meetingList.value!!.openedEvent,
                isDetailOnlyOpen = meetingList.value!!.isDetailOnlyOpen,
                loading = meetingList.value!!.loading,
                error = meetingList.value!!.error
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

class MeetingViewModelPreview(){
    val events = listOf<ApiMeeting>(
        ApiMeeting(
            meetingId = 43,
            meetingGame = "Dnd",
            meetingGenre = "RolePlay",
            meetingStatus = true,
            meetingTitle = "Tyapse",
            meetingOrganizer = "Ilya",
            meetingCountPlayers = 6,
            meetingBody = "Allilya allilya allilayla",
            meetingGeoMarker = "Krusa"
        )
    )
    val meetingList = DunoEventUIState(events = events)
}

data class DunoEventUIState(
    val events: List<ApiMeeting> = emptyList(),
    val favEvents: ApiLikes? = null,
    val openedEvent: ApiMeeting? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)

