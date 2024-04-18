package com.example.duno.db

import com.yandex.mapkit.geometry.Point
import retrofit2.Response
import retrofit2.http.Body
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
        @Path("nickname") name: String?,
    ): ApiUser

    //Проверка пароля на корректность
    @GET("/check_pass")
    suspend fun checkPass(
        @Query("nickname") name: String?,
        @Query("password") password: String?,
    ): Boolean

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

    //Найти все встречи
    @GET("/meeting")
    suspend fun getAllMeetings(): List<ApiMeeting>

    //Создать встречу
    @POST("/meeting")
    suspend fun createMeeting(
        @Body request: ApiMeeting
    ): String

    //Изменить информацию о встрече
    @PUT("/meeting/{meetingID}")
    suspend fun updateMeeting(
        @Path("meetingID") meetingID: Int?,
        @Query("body") body: String?,
        @Query("status") status: Boolean?,
        @Query("geo_marker") geoMarker: String?
    ): String

    //Удалить встречу
    @DELETE("/meeting/{meetingID}")
    suspend fun deleteMeeting(
        @Path("meeting_id") meetingId: Int?
    ): String

    //Понравившиеся встречи
    @DELETE("/likes/{nickname}")
    suspend fun userLikes(
        @Path("nickname") nickname: String?
    ): ApiLikes

    @GET("/more-users")
    suspend fun getMoreUsers(): List<ApiUser>

    @GET("/error")
    suspend fun getUsersWithError(): List<ApiUser>

}


/*private fun JSONMeeting (meeting: ApiMeeting): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("game_name", meeting.meetingGame)
    return jsonObject
}*/
