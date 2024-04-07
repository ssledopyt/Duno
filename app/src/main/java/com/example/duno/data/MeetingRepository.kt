package com.example.duno.data

import com.example.duno.db.ApiMeeting
import com.example.duno.db.ApiService
import com.example.duno.db.DataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface MeetingRepositoryHelper {
    fun getMeeting(meetingID: Int): Flow<DataStatus<ApiMeeting>>
}

@Singleton
class MeetingRepository @Inject constructor(private val apiService: ApiService): MeetingRepositoryHelper{

    override fun getMeeting(meetingID: Int) = flow {
        emit(DataStatus.loading())
        val meetings =  apiService.getMeeting(meetingID)
        emit(DataStatus.success(meetings))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

}
