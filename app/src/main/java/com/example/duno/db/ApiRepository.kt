package com.example.duno.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val apiService: ApiService) :ApiHelper {

    override fun getUser(nickname: String) = flow {
        emit(DataStatus.loading())
        val user = apiService.getUser(nickname)
        emit(DataStatus.success(user))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    override fun getMeeting(meetingID: Int) = flow {
        emit(DataStatus.loading())
        val meetings =  apiService.getMeeting(meetingID)
        emit(DataStatus.success(meetings))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    suspend fun getMoreUsers() = flow {
        emit(apiService.getMoreUsers())
    }

    suspend fun getUsersWithError() = flow {
        emit(apiService.getUsersWithError())
    }

}


interface ApiHelper {

    fun getUser(nickname: String): Flow<DataStatus<ApiUser>>

    fun getMeeting(meetingID: Int): Flow<DataStatus<ApiMeeting>>

 //   fun getMoreUsers(): Flow<List<ApiUser>>

 //   fun getUsersWithError(): Flow<List<ApiUser>>
}