package com.example.duno.data

import com.example.duno.db.ApiLikes
import com.example.duno.db.ApiService
import com.example.duno.db.DataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(private val apiService: ApiService): UserRepositoryHelper {

    override fun getUserLikes(nickname: String) = flow {
        emit(DataStatus.loading())
        val meetings =  apiService.userLikes(nickname)
        emit(DataStatus.success(meetings))
    }.catch {
        emit(DataStatus.error(it.message.toString() ?: "Unknown Error"))
    }
}


interface UserRepositoryHelper {
    fun getUserLikes(nickname: String): Flow<DataStatus<ApiLikes>>
}