package com.example.duno.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val apiService: ApiService) :ApiHelper {

    override fun getUser(nickname: String) = flow {
        emit(DataStatus.loading())
        val user = apiService.getUser(nickname)
        emit(DataStatus.success(user))
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }

    suspend fun getMoreUsers() = flow {
        emit(apiService.getMoreUsers())
    }

    suspend fun getUsersWithError() = flow {
        emit(apiService.getUsersWithError())
    }

}

val apiService = ApiRepository(BDBuilder.apiService)

interface ApiHelper {

    fun getUser(nickname: String): Flow<DataStatus<ApiUser>>

 //   fun getMoreUsers(): Flow<List<ApiUser>>

 //   fun getUsersWithError(): Flow<List<ApiUser>>
}