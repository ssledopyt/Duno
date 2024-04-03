package com.example.duno.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.annotations.SerializedName
import dagger.Component
import java.util.Date


data class Meeting(
    //val id: Int,
    @SerializedName("title")
    val title: String,
 /*   val gameName: String,
    val body: String,
    val status: String,
    val geoMarker: String,
    val countPlayers: String,
    val userNickname: Int,
    val createdAt: Date,
    val closedAt: Date*/
)
