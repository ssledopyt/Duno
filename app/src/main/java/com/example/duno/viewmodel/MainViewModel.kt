package com.example.duno.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.db.ApiMeeting
import com.example.duno.db.ApiRepository
import com.example.duno.db.ApiUser
import com.example.duno.db.BDBuilder
import com.example.duno.db.DataStatus
import com.example.duno.db.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiRepository):ViewModel(){

    private val _meetingList = MutableLiveData<DataStatus<ApiMeeting>>()
    val meetingList : LiveData<DataStatus<ApiMeeting>>
        get() = _meetingList

    val userState = MutableStateFlow(DataStatus(status = DataStatus.Status.LOADING,ApiUser))

    init {
        getMeeting(9)
    }

    fun getUser(nickname: String) = viewModelScope.launch {
        repository.getUser(nickname)
            .catch {ex ->

            }
            .collect{
            //_meetingList.value=it
        }
    }

    fun getMeeting(meetingID: Int) = viewModelScope.launch {
        repository.getMeeting(meetingID)
            .catch {ex ->

            }
            .collect{
                _meetingList.value=it
            }
    }
}
