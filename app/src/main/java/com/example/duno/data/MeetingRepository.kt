package com.example.duno.data

import com.example.duno.db.ApiLikes
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

    override fun getMeeting(meetingID: Int) = flow {
        emit(DataStatus.loading())
        val meeting =  apiService.getMeeting(meetingID)
        emit(DataStatus.success(meeting))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }


    override fun getUserLikes(nickname: String) = flow {
        emit(DataStatus.loading())
        val userLikes =  apiService.getUserLikes(nickname)
        emit(DataStatus.success(userLikes))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun putUserLike(nickname: String, userLike: Int) = flow {
        emit(DataStatus.loading())
        val userLikeObj =  apiService.putUserLikes(nickname, userLike)
        emit(DataStatus.success(userLikeObj))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun deleteUserLike(nickname: String, userLike: Int) = flow {
        emit(DataStatus.loading())
        val userLikeObj =  apiService.deleteUserLikes(nickname, userLike)
        emit(DataStatus.success(userLikeObj))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

}

interface MeetingRepositoryHelper {
    fun getMeeting(meetingID: Int): Flow<DataStatus<ApiMeeting>>
    fun getAllMeetings(): Flow<DataStatus<List<ApiMeeting>>>
    fun getUserLikes(nickname: String): Flow<DataStatus<ApiLikes>>
    fun putUserLike(nickname: String, userLike: Int): Flow<DataStatus<String>>
    fun deleteUserLike(nickname: String, userLike: Int): Flow<DataStatus<String>>
}