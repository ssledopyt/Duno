package com.example.duno

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector


interface DunoDestination {
    val icon: ImageVector
    val route: String
    val text: String
}
object DunoScreens {
    const val MAP_SCREEN = "map"
    const val EVENTS_SCREEN = "events"
    const val USER_EVENTS_SCREEN = "user_events"
    const val ADD_EDIT_EVENT_SCREEN = "addEditEvent"
    const val PROFILE_SCREEN = "profile"
    const val ABOUT_SCREEN = "about"
}

object Events : DunoDestination{
    override val icon = Icons.Filled.Home
    override val route = DunoScreens.EVENTS_SCREEN
    override val text = "Events"
}

object Profile : DunoDestination{
    override val icon = Icons.Filled.Person
    override val route = DunoScreens.PROFILE_SCREEN
    override val text = "Profile"
}

object Map : DunoDestination{
    override val icon = Icons.Filled.Search
    override val route = DunoScreens.MAP_SCREEN
    override val text = "Map"
}

val DunoRowScreens = listOf(Events, Profile, Map)
