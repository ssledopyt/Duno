package com.example.duno.data

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


    override fun getUserLikes(nickname: String) = flow {
        emit(DataStatus.loading())
        val userLikes =  apiService.userLikes(nickname)
        emit(DataStatus.success(userLikes))
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
        name: String,
        secondName: String,
        phone: String,
        email: String,
        password: String,
        nickname: String,
    ) = flow {
        emit(DataStatus.loading())
        val user = apiService.createUser(name,
            secondName,
            phone,
            email,
            password,
            nickname)
        emit(DataStatus.success(user))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

}


interface UserRepositoryHelper {
    fun getUser(nickname: String): Flow<DataStatus<String>>
    fun getUserLikes(nickname: String): Flow<DataStatus<ApiLikes>>
    fun checkPass(nickname: String,  password: String = ""): Flow<DataStatus<String>>
    fun createUser(name: String, secondName: String, phone: String, email: String, password: String, nickname: String): Flow<DataStatus<String>>
}