package com.example.duno.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.db.ApiRepository
import com.example.duno.db.ApiUser
import com.example.duno.db.BDBuilder
import com.example.duno.db.DataStatus
import com.example.duno.db.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiRepository):ViewModel(){

    private val _userList = MutableLiveData<DataStatus<ApiUser>>()
    val userList : LiveData<DataStatus<ApiUser>> = _userList

    val userState = MutableStateFlow(DataStatus(status = DataStatus.Status.LOADING,ApiUser))
    private val userRepository = ApiRepository(BDBuilder.apiService)

    init {
        getUser("tah")
    }

    fun getUser(nickname: String) = viewModelScope.launch {
        repository.getUser(nickname).collect{
            _userList.value=it
        }
    }
}
