package com.example.duno.data

import com.example.duno.db.ApiGame
import com.example.duno.db.ApiGenre
import com.example.duno.db.ApiLikes
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

}


interface UserRepositoryHelper {
    fun getUser(nickname: String): Flow<DataStatus<ApiUser>>
    fun checkPass(nickname: String,  password: String = ""): Flow<DataStatus<String>>
    fun createUser(user: ApiUser): Flow<DataStatus<String>>
    fun loadGames():Flow<DataStatus<List<ApiGame>>>
    fun loadGenres():Flow<DataStatus<List<ApiGenre>>>

}