package com.example.duno

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector


interface DunoDestination {
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val route: String
    val text: String
}
object DunoScreens {
    const val MAP_SCREEN = "Map"
    const val EVENTS_SCREEN = "Events"
    const val USER_EVENTS_SCREEN = "user_events"
    const val ADD_EDIT_EVENT_SCREEN = "addEditEvent"
    const val PROFILE_SCREEN = "Profile"
    const val ABOUT_SCREEN = "about"
}

object Events : DunoDestination{
    override val selectedIcon = Icons.Filled.Home
    override val unselectedIcon = Icons.Outlined.Home
    override val route = DunoScreens.EVENTS_SCREEN
    override val text = "Мероприятия"
}

object Profile : DunoDestination{
    override val selectedIcon = Icons.Filled.Person
    override val unselectedIcon = Icons.Outlined.Person
    override val route = DunoScreens.PROFILE_SCREEN
    override val text = "Профиль"
}

object Map : DunoDestination{
    override val selectedIcon = Icons.Filled.Search
    override val unselectedIcon = Icons.Outlined.Search
    override val route = DunoScreens.MAP_SCREEN
    override val text = "Карты"
}

val DunoRowScreens = listOf(Map, Events, Profile)
