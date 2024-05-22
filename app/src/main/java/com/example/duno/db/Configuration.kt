package com.example.duno.db

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point
import java.sql.Timestamp
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
    val userCreatedAt:String,
){
    companion object Pavel{
        val userId = 0
        val userName = "llfa"
        val userSecondName = "DSF"
        val userNickname = "42433"
        val userPhone = "333"
        val userEmail = "32"
        val userPassword = "424"
        val userCreatedAt = String
    }
}


data class ApiMeeting(
    @SerializedName("meeting_id")
    val meetingId: Int? = null,
    @SerializedName("title")
    val meetingTitle: String = "",
    @SerializedName("game")
    val meetingGame: String = "",
    @SerializedName("genre")
    val meetingGenre: String = "",
    @SerializedName("body")
    var meetingBody: String = "",
    @SerializedName("organizer")
    val meetingOrganizer: String = "",
    @SerializedName("status")
    var meetingStatus: Boolean = true,
    @SerializedName("geo_marker")
    val meetingGeoMarker: List<Double>,
    @SerializedName("count_players")
    val meetingCountPlayers: Int = 0,
    @SerializedName("meeting_time")
    val meetingDate: String,
    @SerializedName("name_of_club")
    val clubName: String = "",
){
    companion object Ilya{
        val meetingId = 0
        val meetingTitle = "Поход в Тильтамир"
        val meetingGame = "Dungeon and Dragons"
        val meetingBody = "Собираемся в клубе около 12, может быть за час. Кто хочет пишите мне в телеграм. Возьмите с собой все нужные карты, фишки"
        val meetingOrganizer = "Игорь"
        val meetingStatus = true
        val meetingGenre = "RolePlay"
        val meetingGeoMarker = Point(55.963143, 38.044838)
        val meetingCountPlayers = 3
        val meetingCreated = "String"
        val meetingDate = "2024-04-29T18:28:45.000"
        val clubName = "12 Ярдов"
    }
}

data class ApiGenre(
/*    @SerializedName("genre_id")
    val genreId: Int = 0,*/
    @SerializedName("name")
    val gameGenre: String = ""
)

data class ApiLocationOfSP(
    @SerializedName("meet_id")
    val meetId: Int = 0,
    @SerializedName("name_of_club")
    val nameClub: String = "",
    @SerializedName("geo_marker")
    val geoMarker: List<Double>,
    @SerializedName("place")
    val place: String = ""
){
    companion object Terra{
        val meetId = 0
        val nameClub = "Терра"
        val geoMarker = listOf(55.77216, 37.6826)
        val place = "42433"
    }
}


data class ApiGame(
    @SerializedName("game_id")
    val gameId: Int = 0,
    @SerializedName("game_name")
    val gameName: String = "Информация отсутствует",
    @SerializedName("genre")
    val gameGenre: String = "Информация отсутствует",
    @SerializedName("count_players")
    val gameCountPlayers: Int = 0,
    @SerializedName("game_time")
    val gameTime: String = "Информация отсутствует",
    @SerializedName("description")
    val gameDescription: String = "Информация отсутствует"
)

data class ApiLikes(
    /*    @SerializedName("nickname")
        val nickname: String = "",*/
    @SerializedName("meeting_id")
    val meetingId: List<Int> = emptyList(),
)



