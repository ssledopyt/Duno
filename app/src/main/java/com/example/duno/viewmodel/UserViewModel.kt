package com.example.duno.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.data.UserRepository
import com.example.duno.db.ApiMeeting
import com.example.duno.db.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val session: Session
): ViewModel() {
/*
    var userNameInput by mutableStateOf(LoginState())
    var userPasswordInput by mutableStateOf(LoginState())
*/
    val isLoggedIn = session.isUserLoggedIn()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)
    val userName = session.getUserName()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = "")
    val userPassword = session.getPassword()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = "")

    fun getIsLoggedIn() = viewModelScope.launch {
        isUserLoggedIn = isLoggedIn.value
    }

    private val _userState = MutableLiveData(DunoUserUIState(loading = true))
        val userState : LiveData<DunoUserUIState>
            get() = _userState

    var isUserLoggedIn by mutableStateOf(false)
    var userLoggedMessage by mutableStateOf("")

    fun loginToServer(userNameInput:String, userPasswordInput:String) {
        if (userNameInput.isEmpty() || userNameInput.isBlank()) {
            _userState.value = DunoUserUIState(isError = true, error = "Please Enter UserName")
            return
        }
        if (userPasswordInput.isBlank() || userPasswordInput.isEmpty()) {
            _userState.value = DunoUserUIState(isError = true, error = "Please Enter Password")
            return
        }
        viewModelScope.launch {
            val response = repository.checkPass(
              nickname = userNameInput,
              password = userPasswordInput
            ).catch {
                Timber.e(it.message)
                _userState.value = DunoUserUIState(isError = true, error = "Unknown Error")
            }.collect{
                if (it.data.toBoolean()){
                    _userState.value = DunoUserUIState(isError = false)
                }
                else{
                    _userState.value = DunoUserUIState(isError = true, error = "Неверный логин или пароль!")
                }
                Timber.e("checkPass successful!!")
            }
            if (userState.value!!.isError){
                return@launch
            }
            session.setUserLoggedIn(true)
            session.setPassword(userPasswordInput)
            session.setUserName(userNameInput)
            isUserLoggedIn = true
            userLoggedMessage="$userNameInput has successfully logged in!"
            Timber.e(session.getUserName().toString())
        }

    }

    fun getUserLikes(nickname: String) = viewModelScope.launch {
        repository.getUserLikes(nickname)
            .catch {
                _userState.value= DunoUserUIState(error = it.message)
            }.collect{
                _userState.value= DunoUserUIState(
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
//    val isUser: Boolean = false,
    val isError: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)


/*
@Parcelize
data class LoginState(
    val input: String = "",
    val isError: Boolean = false,
    val errorText: String = ""
) : Parcelable*/
