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
    fun getAllMeetings(): Flow<DataStatus<List<ApiMeeting>>>
    fun createMeeting(meeting: ApiMeeting): Flow<DataStatus<String>>
}

@Singleton
class MeetingRepository @Inject constructor(private val apiService: ApiService): MeetingRepositoryHelper{

    override fun createMeeting(meeting: ApiMeeting) = flow {
        emit(DataStatus.loading())
        val meetings =  apiService.createMeeting(meeting)
        emit(DataStatus.success(meetings))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun getAllMeetings() = flow {
        emit(DataStatus.loading())
        val meetings =  apiService.getAllMeetings()
        emit(DataStatus.success(meetings))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun getMeeting(meetingID: Int) = flow {
        emit(DataStatus.loading())
        val meeting =  apiService.getMeeting(meetingID)
        emit(DataStatus.success(meeting))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

}
