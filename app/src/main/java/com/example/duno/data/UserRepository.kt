package com.example.duno.data

import com.example.duno.db.ApiGame
import com.example.duno.db.ApiGenre
import com.example.duno.db.ApiLikes
import com.example.duno.db.ApiLocationOfSP
import com.example.duno.db.ApiMeeting
import com.example.duno.db.ApiService
import com.example.duno.db.ApiUser
import com.example.duno.db.DataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(private val apiService: ApiService): UserRepositoryHelper {


    override fun getUser(nickname: String) = flow {
        emit(DataStatus.loading())
        val user =  apiService.getUser(nickname)
        emit(DataStatus.success(user))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun checkPass(nickname: String, password: String) = flow {
        emit(DataStatus.loading())
        val user = apiService.checkPass(nickname, password)
        emit(DataStatus.success(user))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    override fun createUser(
        user: ApiUser
    ) = flow {
        emit(DataStatus.loading())
        val user = apiService.createUser(name = user.userName,
            secondName = user.userSecondName,
            phone = user.userPhone,
            email = user.userEmail,
            password = user.userPassword,
            nickname = user.userNickname)
        emit(DataStatus.success(user))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    override fun loadGames() = flow {
        emit(DataStatus.loading())
        val game =  apiService.getAllGames()
        emit(DataStatus.success(game))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun loadGenres() = flow {
        emit(DataStatus.loading())
        val genre =  apiService.getAllGenre()
        emit(DataStatus.success(genre))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun loadPlaces() = flow {
        emit(DataStatus.loading())
        val genre =  apiService.getAllPlaces()
        emit(DataStatus.success(genre))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }

    override fun createMeeting(meeting: ApiMeeting) = flow {
        emit(DataStatus.loading())
        val newMeeting =  apiService.createMeeting(
            title = meeting.meetingTitle,
            gameName = meeting.meetingGame,
            body = meeting.meetingBody,
            status = meeting.meetingStatus,
            geoMarker = meeting.meetingGeoMarker.toString(),
            nickname = meeting.meetingOrganizer,
            countPlayers = meeting.meetingCountPlayers,
            meetingTime = meeting.meetingDate,
            genre = meeting.meetingGenre
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

    override fun updateMeeting(meetingID: Int, body:String, status:Boolean) = flow {
        emit(DataStatus.loading())
        val meeting =  apiService.updateMeeting(meetingID, body, status)
        emit(DataStatus.success(meeting))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }
}


interface UserRepositoryHelper {
    fun getUser(nickname: String): Flow<DataStatus<ApiUser>>
    fun checkPass(nickname: String,  password: String = ""): Flow<DataStatus<String>>
    fun createUser(user: ApiUser): Flow<DataStatus<String>>
    fun createMeeting(meeting: ApiMeeting): Flow<DataStatus<String>>
    fun deleteMeeting(meetingID: Int): Flow<DataStatus<String>>
    fun updateMeeting(meetingID: Int, body:String, status:Boolean): Flow<DataStatus<String>>


    fun loadPlaces():Flow<DataStatus<List<ApiLocationOfSP>>>
    fun loadGames():Flow<DataStatus<List<ApiGame>>>
    fun loadGenres():Flow<DataStatus<List<ApiGenre>>>
}