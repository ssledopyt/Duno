package com.example.duno.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.db.ApiLikes
import com.example.duno.db.ApiMeeting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(private val repository: MeetingRepository):ViewModel(){
    private val _meetingList = MutableLiveData(DunoEventUIState(loading = true))
    val meetingList : LiveData<DunoEventUIState>
        get() = _meetingList

    private val _meetingListFavorite = MutableLiveData(DunoEventUIState(loading = true))
    val meetingListFavorite : LiveData<DunoEventUIState>
        get() = _meetingListFavorite

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
    val favEvents: List<ApiLikes> = emptyList(),
    val selectedEmails: Set<Int> = emptySet(),
    val openedEmail: ApiMeeting? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)