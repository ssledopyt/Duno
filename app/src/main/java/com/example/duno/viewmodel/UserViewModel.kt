package com.example.duno.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.data.UserRepository
import com.example.duno.db.ApiGame
import com.example.duno.db.ApiGenre
import com.example.duno.db.ApiLocationOfSP
import com.example.duno.db.ApiMeeting
import com.example.duno.db.ApiUser
import com.example.duno.db.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import timber.log.Timber
import java.sql.Timestamp
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
    val userNickname = session.getNickname()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = "")

    private val _userState = MutableLiveData(DunoUserUIState(loading = true))
    val userState : LiveData<DunoUserUIState>
        get() = _userState

    //Игры
    private val _games = MutableLiveData<List<ApiGame>?>(emptyList())
    val games : LiveData<List<ApiGame>?>
        get() = _games

    //Жанры
    private val _genre = MutableLiveData<List<ApiGenre>?>(emptyList())
    val genre : LiveData<List<ApiGenre>?>
        get() = _genre

    //Места клубов
    private val _places = MutableLiveData<List<ApiLocationOfSP>?>(emptyList())
    val places : LiveData<List<ApiLocationOfSP>?>
        get() = _places

    var isUserLoggedIn by mutableStateOf(false)
    var userLoggedMessage by mutableStateOf("")
    var userLoggedNickname by mutableStateOf("")


    fun getIsLoggedIn() = viewModelScope.launch {
        isUserLoggedIn = isLoggedIn.value
    }


    //Авторизация пользователя
    fun loginToServer(userNicknameInput:String, userPasswordInput:String) {
        if (userNicknameInput.isEmpty() || userNicknameInput.isBlank()) {
            _userState.value = DunoUserUIState(isError = true, error = "Please Enter UserName")
            return
        }
        if (userPasswordInput.isBlank() || userPasswordInput.isEmpty()) {
            _userState.value = DunoUserUIState(isError = true, error = "Please Enter Password")
            return
        }
        viewModelScope.launch {
            val response = repository.checkPass(
              nickname = userNicknameInput,
              password = userPasswordInput
            ).catch {
                Timber.e(it)
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
            session.setNickname(userNicknameInput)
            getUser(nickname = userNicknameInput)
/*            repository.getUser(userNicknameInput).catch {
                session.setUserName("Kolya")
                Timber.e("mess")
            }.collect{
                Timber.e(it.data.toString())
                session.setUserName(it.data!!.userName)
            }*/
            isUserLoggedIn = true
            userLoggedNickname = userNicknameInput
            userLoggedMessage="$userLoggedNickname has successfully logged in!"
            Timber.e(session.getUserName().toString())
        }

    }


    //Регистрация пользователя с последующим входом
    fun userRegistration(user: ApiUser) = viewModelScope.launch {
        repository.createUser(user)
            .catch {
                Timber.tag("UserVM/userRegistration").e(it)
            }
            .collect{
                if (it.data.toBoolean()){
                    _userState.value = DunoUserUIState(isError = false)
                }
                else{
                    _userState.value = DunoUserUIState(isError = true, error = "Такой пользователь уже существует")
                }
                Timber.e("creating user successful!!")
        }
        if (userState.value!!.isError){
            return@launch
        }
        session.setUserLoggedIn(true)
        session.setNickname(user.userNickname)
        session.setUserName(user.userName)
        isUserLoggedIn = true
        userLoggedNickname = user.userNickname
        userLoggedMessage="$userLoggedNickname has successfully logged in!"
        Timber.e(session.getUserName().toString())
    }


    fun getUser(nickname: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.getUser(nickname).catch {
            //nothing?
            Timber.e(it.message)
        }.collect{
            Timber.e(it.data.toString())
            it.data?.userName?.let { it1 -> session.setUserName(it1) }
        }
    }


    //Выход пользователя из системы
    fun logOutUser(){
        viewModelScope.launch {
            userLoggedMessage="${userName.value} has successfully logged in!"
            session.setUserLoggedIn(false)
            session.setNickname("")
            session.setUserName("")
            isUserLoggedIn = false
            Timber.tag("UserName in session").e(session.getUserName().toString())
        }
    }

    fun getAllGenre() = viewModelScope.launch() {
        repository.loadGenres().catch {
            Timber.e(it.message)
        }.collect{
            Timber.e(it.data.toString())
            _genre.value = it.data
        }
    }

    fun getAllPlaces() = viewModelScope.launch() {
        repository.loadPlaces().catch {
            //nothing?
            Timber.e(it.message)
        }.collect{
            //Timber.e(it.data.toString())
            _places.value = it.data
        }
    }

    fun getAllGame() = viewModelScope.launch() {
        repository.loadGames().catch {
            //nothing?
            Timber.e(it.message)
        }.collect{
            Timber.e(it.data.toString())
            _games.value = it.data
        }
    }


    fun createUserMeeting(meeting: ApiMeeting) = viewModelScope.launch() {
        val meet = meeting.copy(meetingOrganizer = userNickname.value)
        repository.createMeeting(meet).catch {
            //nothing?
            Timber.tag("ErrorUserVM").e(it.message)
        }.collect{
            Timber.tag("CollectUserVM").e(it.data.toString())
        }
    }

    fun deleteUserMeeting(meetingId: Int) = viewModelScope.launch() {
        repository.deleteMeeting(meetingId).catch {
            //nothing?
            Timber.tag("ErrorUserVM").e(it.message)
        }.collect{
            Timber.tag("CollectUserVM").e(it.data.toString())
        }
    }

    fun updateUserMeeting(meetingID: Int, body:String, status:Boolean) = viewModelScope.launch() {
        repository.updateMeeting(meetingID, body, status).catch {
            //nothing?
            Timber.tag("ErrorUserVM").e(it.message)
        }.collect{
            Timber.tag("CollectUserVM").e(it.data.toString())
        }
    }
}

/*class UserViewModelPreview(){
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
            meetingGeoMarker = "Krusa",
            meetingDate = "Timestamp(System.currentTimeMillis())",
            meetingClosed = "Timestamp(System.currentTimeMillis())",
        )
    )

    val meetingList = DunoUserUIState(favEvents = favEvents, myEvents = myEvents)
}*/

data class DunoUserUIState(
    val userName: String = "",
    val favEvents: List<Int> = emptyList(),
    val myEvents: List<ApiMeeting> = emptyList(),
//    val isUser: Boolean = false,
    val isError: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)