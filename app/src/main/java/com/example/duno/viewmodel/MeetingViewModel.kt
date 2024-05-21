package com.example.duno.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.data.MeetingRepository
import com.example.duno.db.ApiLikes
import com.example.duno.db.ApiMeeting
import com.example.duno.db.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(private val repository: MeetingRepository):ViewModel(){
    private val _meetingList = MutableLiveData(DunoEventUIState(loading = true))
    val meetingList : LiveData<DunoEventUIState>
        get() = _meetingList

    //var userLikes by mutableListOf<Int>(44)

    init {
        getAllMeetings()
    }

    fun getAllMeetings() = viewModelScope.launch {
        Timber.tag("MeetingViewModel").e("get all meetings")
        repository.getAllMeetings()
            .catch {
                _meetingList.value = _meetingList.value!!.copy(error = it.message)
            }.collect{
                Timber.tag("MeetingViewModel").e(it.data.toString())
                _meetingList.value= _meetingList.value!!.copy(
                    events = if (it.data.isNullOrEmpty()) emptyList() else it.data,
                )
            }
    }

    fun getUserLikes(nickname:String) = viewModelScope.launch {
        repository.getUserLikes(nickname).catch {
            _meetingList.value = _meetingList.value!!.copy(error = it.message)
        }.collect{
            _meetingList.value = _meetingList.value!!.copy(
                favEvents = if (it.data == null) emptyList() else it.data.meetingId
            )
        }
    }

    fun putUserLikes(nickname: String, like: Int?) = viewModelScope.launch {
        if (like!=null){
            Timber.tag("MeetingViewModel").e("putLike")
            repository.putUserLike(nickname, like)
                .catch {
                    _meetingList.value = _meetingList.value!!.copy(error = it.message)
                }.collect{
                    Timber.tag("MeetingViewModel").e(it.data.toString())
                    //var list = ApiLikes(meetingList.value!!.favEvents!!.meetingId.plus(userList))
                    _meetingList.value= _meetingList.value!!.copy(
                        favEvents = meetingList.value!!.favEvents.plus(like))
                }
        }
    }

    fun deleteUserLikes(nickname: String, unlike: Int?) = viewModelScope.launch {
        if (unlike!=null) {
            Timber.tag("MeetingViewModel").e("deleteLike")
            repository.deleteUserLike(nickname, unlike)
                .catch {
                    _meetingList.value = _meetingList.value!!.copy(error = it.message)
                }.collect {
                    Timber.tag("MeetingViewModel").e(it.data.toString())
                    //var list = ApiLikes(meetingList.value!!.favEvents!!.meetingId.plus(userList))
                    _meetingList.value = _meetingList.value!!.copy(
                        favEvents = meetingList.value!!.favEvents.minus(unlike)
                    )
                }
        }
    }

    fun updateOrDelete(meeting: ApiMeeting, delete:Boolean, body:String?, status:Boolean?) =viewModelScope.launch {
        if (delete){
            _meetingList.value = _meetingList.value!!.copy(
                events =  _meetingList.value!!.events.minus(meeting)
            )
        }else{
            meetingList.value!!.events.find { meeting == it}?.meetingBody = body!!
            meetingList.value!!.events.find { meeting == it}?.meetingStatus = status!!
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


/*class MeetingViewModelPreview(){
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
            meetingGeoMarker = List<Float>
            meetingDate = "Timestamp(System.currentTimeMillis())",
            meetingClosed = "Timestamp(System.currentTimeMillis())",
            )
    )
    val meetingList = DunoEventUIState(events = events)
}*/

data class DunoEventUIState(
    val events: List<ApiMeeting> = emptyList(),
    var favEvents: List<Int> = emptyList(),
    val openedEvent: ApiMeeting? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)

