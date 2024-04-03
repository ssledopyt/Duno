package com.example.duno.db

import com.example.duno.data.Meeting
import com.yandex.mapkit.geometry.Point
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Найти пользователя
    @GET("/user")
    suspend fun getUser(
        @Query("nickname") name: String?,
    ): ApiUser

    //Создать пользователя
    @POST("/user")
    suspend fun createUser(
        @Query("name") name: String?,
        @Query("second_name") secondName: String?,
        @Query("phone") phone: String?,
        @Query("email") email: String?,
        @Query("password") password: String?,
    ): List<ApiUser>

    //Изменить данные пользователя
    @PUT("/user")
    suspend fun changeUserInformation(
        @Query("name") name: String?,
        @Query("email") email: String?,
        @Query("password") password: String?
    ): List<ApiUser>

    //Найти встречу
    @GET("/meeting/{meetingID}")
    suspend fun getMeeting(
        @Path("meetingID") meetingID: Int?,
    ): ApiMeeting

    //Создать встречу
    @POST("/meeting")
    suspend fun createMeeting(meeting: ApiMeeting): Response<ApiMeeting>

    //Изменить информацию о встрече
    @PUT("/meeting")
    suspend fun changeMeetingInformation(
        @Query("body") name: String?,
        @Query("status") email: Boolean?,
        @Query("geo_marker") password: Point?
    ): Response<ApiMeeting>

    //Удалить встречу
    @DELETE("/meeting")
    suspend fun deleteMeeting(
        @Query("meeting_id") meetingId: Int?
    ): Response<ApiMeeting>

    @GET("/more-users")
    suspend fun getMoreUsers(): List<ApiUser>

    @GET("/error")
    suspend fun getUsersWithError(): List<ApiUser>

}

/*interface ApiHelper {

    fun getUser(user: ApiUser): Flow<DataStatus<List<ApiUser>>>

    fun getMoreUsers(): Flow<List<ApiUser>>

    fun getUsersWithError(): Flow<List<ApiUser>>

}*/


