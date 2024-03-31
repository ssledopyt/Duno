package com.example.duno.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duno.db.ApiRepository
import com.example.duno.db.ApiUser
import com.example.duno.db.DataStatus
import com.example.duno.db.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiRepository):ViewModel(){

    private val _userList = MutableLiveData<DataStatus<User>>()
    val userList : LiveData<DataStatus<User>> = _userList

    fun getUser(user: ApiUser) = viewModelScope.launch {
        repository.getUser(user).collect{user
            _userList.value=
        }
    }
}