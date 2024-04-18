package com.example.duno.data

import com.example.duno.db.ApiMeeting
import com.example.duno.db.ApiService
import com.example.duno.db.ApiUser
import com.example.duno.db.DataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


//NOT WORKING APIREP -> NOT USING ANYWHERE
@Singleton
class ApiRepository @Inject constructor(private val apiService: ApiService) : ApiHelper {

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