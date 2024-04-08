package com.example.duno.data

import com.example.duno.db.ApiMeeting
import com.example.duno.db.ApiService
import com.example.duno.db.DataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingRepository @Inject constructor(private val apiService: ApiService): MeetingRepositoryHelper{

    override fun getAllMeetings() = flow {
        emit(DataStatus.loading())
        val meetings =  apiService.getAllMeetings()
        emit(DataStatus.success(meetings))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    override fun createMeeting(meeting: ApiMeeting) = flow {
        emit(DataStatus.loading())
        val newMeeting =  apiService.createMeeting(meeting)
        emit(DataStatus.success(newMeeting))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    override fun getMeeting(meetingID: Int) = flow {
        emit(DataStatus.loading())
        val meeting =  apiService.getMeeting(meetingID)
        emit(DataStatus.success(meeting))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    override fun updateMeeting(meetingID: Int, meeting: ApiMeeting) = flow {
        emit(DataStatus.loading())
        val newMeeting =  apiService.updateMeeting(
            meetingID=meetingID,
            body = meeting.meetingTitle,
            status = meeting.meetingStatus,
            geoMarker =meeting.meetingGeoMarker,
        )
        emit(DataStatus.success(newMeeting))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    override fun deleteMeeting(meetingID: Int) = flow {
        emit(DataStatus.loading())
        val meeting =  apiService.deleteMeeting(meetingID)
        emit(DataStatus.success(meeting))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

}

interface MeetingRepositoryHelper {
    fun getMeeting(meetingID: Int): Flow<DataStatus<ApiMeeting>>
    fun getAllMeetings(): Flow<DataStatus<List<ApiMeeting>>>
    fun createMeeting(meeting: ApiMeeting): Flow<DataStatus<String>>
    fun updateMeeting(meetingID: Int, meeting: ApiMeeting): Flow<DataStatus<String>>
    fun deleteMeeting(meetingID: Int): Flow<DataStatus<String>>
}