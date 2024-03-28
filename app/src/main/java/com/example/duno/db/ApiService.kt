package com.example.duno.db

import android.provider.ContactsContract.CommonDataKinds.Nickname
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

import javax.inject.Singleton

interface ApiService {

    @GET("select_user_information")
    suspend fun getUser(@Query("nickname") nickname: String): List<ApiUser>

    @GET("more-users")
    suspend fun getMoreUsers(): List<ApiUser>

    @GET("error")
    suspend fun getUsersWithError(): List<ApiUser>

}

interface ApiHelper {

    fun getUser(nickname: String): Flow<List<ApiUser>>

    fun getMoreUsers(): Flow<List<ApiUser>>

    fun getUsersWithError(): Flow<List<ApiUser>>

}

@Singleton
class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override fun getUser(nickname: String) = flow {
        emit(apiService.getUser(nickname))
    }

    override fun getMoreUsers() = flow {
        emit(apiService.getMoreUsers())
    }

    override fun getUsersWithError() = flow {
        emit(apiService.getUsersWithError())
    }

}

val apiHelper = ApiHelperImpl(RetrofitBuilder.apiService)

