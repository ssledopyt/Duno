package com.example.duno.data

import com.example.duno.db.ApiService
import com.example.duno.db.DataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/*
interface MeetingRepositoryInt {
    fun getSelectedMeeting(): Flow<Meeting>
    fun getAllMeeting(): Flow<List<Meeting>>
    fun getGeoMeeting(uid: Long): Flow<Meeting>
}*/

@Singleton
open class MeetingRepository @Inject constructor(private val apiService: ApiService){

/*    suspend fun getMeetingList() = flow {
            emit()
            val result = apiService
        }*/

}
