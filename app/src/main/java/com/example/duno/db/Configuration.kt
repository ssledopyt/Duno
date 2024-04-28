package com.example.duno.db

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point
import java.time.LocalDate
import java.time.format.DateTimeFormatter


var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

class User : ArrayList<ApiUser>()
data class ApiUser(
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("name")
    val userName: String = "",
    @SerializedName("second_name")
    val userSecondName: String = "",
    @SerializedName("nickname")
    val userNickname: String = "",
    @SerializedName("phone")
    val userPhone: String = "",
    @SerializedName("email")
    val userEmail: String = "",
    @SerializedName("password")
    val userPassword: String = "",
    @SerializedName("created_at")
    val userCreatedAt: LocalDate = LocalDate.parse("31-12-2018", formatter),
){
    companion object Pavel{
        val userId = 0
        val userName = "llfa"
        val userSecondName = "DSF"
        val userNickname = "42433"
        val userPhone = "333"
        val userEmail = "32"
        val userPassword = "424"
        val userCreatedAt = LocalDate.parse("31-12-2028", formatter)
    }
}


data class ApiMeeting(
    @SerializedName("meeting_id")
    val meetingId: Int = 0,
    @SerializedName("title")
    val meetingTitle: String = "",
    @SerializedName("game")
    val meetingGame: String = "",
    @SerializedName("genre")
    val meetingGenre: String = "",
    @SerializedName("body")
    val meetingBody: String = "",
    @SerializedName("organizer")
    val meetingOrganizer: String = "",
    @SerializedName("status")
    val meetingStatus: Boolean = true,
    @SerializedName("geo_marker")
    val meetingGeoMarker: String,
    @SerializedName("count_players")
    val meetingCountPlayers: Int = 0,
    @SerializedName("meeting_time")
    val meetingDate: LocalDate = LocalDate.parse("31-12-2018", formatter),
    @SerializedName("closed_at")
    val meetingClosed: LocalDate = LocalDate.parse("31-12-2018", formatter),
){
    companion object Ilya{
        val meetingId = 0
        val meetingTitle = "Поход в Тильтамир"
        val meetingGame = "Тильтамир"
        val meetingBody = "Собираемся в клубе около 12, может быть за час. Кто хочет пишите мне в телеграм. Возьмите с собой все нужные карты, фишки"
        val meetingOrganizer = "Игорь"
        val meetingStatus = true
        val meetingGenre = "Dungeon and Dragons"
        val meetingGeoMarker = Point(55.963143, 38.044838)
        val meetingCountPlayers = 3
        val meetingCreated = LocalDate.parse("31-12-2028", formatter)
        val meetingClosed = LocalDate.parse("31-12-2028", formatter)
    }
}

data class ApiGenre(
    @SerializedName("genre_id")
    val genreId: Int = 0,
    @SerializedName("name")
    val gameGenre: String = ""
)

data class ApiLocationOfSP(
    @SerializedName("meet_id")
    val meetId: Int = 0,
    @SerializedName("name_of_club")
    val nameClub: String = "",
    @SerializedName("geo_marker")
    val geoMarker: Point,
    @SerializedName("place")
    val place: String = ""
)

data class ApiGame(
    @SerializedName("game_id")
    val gameId: Int = 0,
    @SerializedName("game_name")
    val gameName: String = "",
    @SerializedName("genre")
    val gameGenre: String = "",
    @SerializedName("count_players")
    val gameCountPlayers: Byte = 0,
    @SerializedName("game_time")
    val gameTime: Byte = 0,
    @SerializedName("description")
    val gameDescription: String = ""
)

data class ApiLikes(
    /*    @SerializedName("nickname")
        val nickname: String = "",*/
    @SerializedName("meeting_id")
    val meetingId: List<Int> = emptyList(),
)



