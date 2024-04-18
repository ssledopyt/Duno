package com.example.duno.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.db.ApiMeeting
import com.example.duno.data.ApiRepository
import com.example.duno.db.ApiUser
import com.example.duno.db.DataStatus
import com.example.duno.db.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiRepository, session: Session):ViewModel(){

    val isLoggedIn = session.isUserLoggedIn()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)
    val userName = session.getUserName()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = "")
    val userPassword = session.getPassword()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = "")


    val userState = MutableStateFlow(DataStatus(status = DataStatus.Status.LOADING,ApiUser))

    init {
    }

    fun getUser(nickname: String) = viewModelScope.launch {
        repository.getUser(nickname)
            .catch {ex ->

            }
            .collect{
            //_meetingList.value=it
        }
    }
}
