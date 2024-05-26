package com.example.duno.db

import com.yandex.mapkit.geometry.Point
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    //Найти пользователя
    @GET("/user/{nickname}")
    suspend fun getUser(
        @Path("nickname") name: String?,
    ): ApiUser

    //Проверка пароля на корректность (вход уже существующего пользователя)
    @GET("/check_pass")
    suspend fun checkPass(
        @Query("nickname") name: String?,
        @Query("password") password: String?,
    ): String

    //Создать нового пользователя
    @POST("/user")
    suspend fun createUser(
        @Query("name") name: String?,
        @Query("second_name") secondName: String?,
        @Query("phone") phone: String?,
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("nickname") nickname: String?,
    ): String

    @POST("/meeting/add")
    suspend fun createMeeting(
        @Query("title") title: String?,
        @Query("game_name") gameName: String?,
        @Query("body") body: String?,
        @Query("status") status: Boolean?,
        @Query("genre") genre: String?,
        @Query("geo_marker") geoMarker: String,
        @Query("user_nickname") nickname: String?,
        @Query("count_players") countPlayers: Int?,
        @Query("meeting_time") meetingTime: String?,
    ): String

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

    //Получить все жанры
    @GET("/genre")
    suspend fun getAllGenre(): List<ApiGenre>

    //Получить все игры
    @GET("/games")
    suspend fun getAllGames(): List<ApiGame>

    //Получить все места
    @GET("/places")
    suspend fun getAllPlaces(): List<ApiLocationOfSP>

    //Изменить информацию о встрече
    @PUT("/meeting/{meetingID}")
    suspend fun updateMeeting(
        @Path("meetingID") meetingID: Int?,
        @Query("body") body: String?,
        @Query("status") status: Boolean?,
    ): String

    //Удалить встречу
    @DELETE("/meeting/{meetingId}")
    suspend fun deleteMeeting(
        @Path("meetingId") meetingId: Int?
    ): String

    //Понравившиеся встречи
    @GET("/likes/{nickname}")
    suspend fun getUserLikes(
        @Path("nickname") nickname: String
    ): ApiLikes

    //исправил с делит на гет

    @POST("/likes/{nickname}/{eventId}")
    suspend fun putUserLikes(
        @Path("nickname") nickname: String,
        @Path("eventId") eventId: Int,
        //@Field("items[]") list: List<Int>
    ): String

    @DELETE("/likes/{nickname}/{eventId}")
    suspend fun deleteUserLikes(
        @Path("nickname") nickname: String,
        @Path("eventId") eventId: Int,
    ): String

    //отправка Json

}
