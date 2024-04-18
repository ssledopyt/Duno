package com.example.duno.viewmodel

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.data.UserRepository
import com.example.duno.db.ApiMeeting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {
    private val _meetingListFavorite = MutableLiveData(DunoUserUIState(loading = true))
    val meetingListFavorite : LiveData<DunoUserUIState>
        get() = _meetingListFavorite

    fun getUserLikes(nickname: String) = viewModelScope.launch {
        repository.getUserLikes(nickname)
            .catch {
                _meetingListFavorite.value= DunoUserUIState(error = it.message)
            }.collect{
                _meetingListFavorite.value= DunoUserUIState(
                    favEvents = it.data?.meetingId?: emptyList(),
                    loading = false
                )
            }
    }

}

class UserViewModelPreview(){
    private val favEvents = listOf<Int>(1,2,5)
    private val myEvents = listOf<ApiMeeting>(
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

    val meetingList = DunoUserUIState(favEvents = favEvents, myEvents = myEvents)
}

data class DunoUserUIState(
    val favEvents: List<Int> = emptyList(),
    val myEvents: List<ApiMeeting> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)