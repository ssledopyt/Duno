package com.example.duno.compose.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.duno.db.ApiGame
import com.example.duno.viewmodel.UserViewModel
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(viewModel: UserViewModel = hiltViewModel()) {
    var eventName by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf<ApiGame?>(null) }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var playerCount by remember { mutableStateOf(0) }
    var gatheringTime by remember { mutableStateOf(LocalTime.now()) }
    var expanded by remember { mutableStateOf(true) }

    Column {
        TextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Название мероприятия") }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (selectedGame == null) viewModel.getAllGame()
            if (viewModel.games.value.isNullOrEmpty()) {
                viewModel.games.value!!.forEach { game ->
                    DropdownMenuItem(
                        text = {game.gameName},
                        onClick = {
                            selectedGame = game }
                    )
                }
            } else {
                Text("Игры загружаются...")
            }
        }

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Место проведения") }
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") }
        )

        Row {
            Text("Количество игроков:")
            Spacer(Modifier.width(8.dp))
/*            NumberPicker(
                value = playerCount,
                onValueChange = { playerCount = it },
                range = 1..16
            )*/
        }

/*        DatePicker(
            time = gatheringTime,
            onTimeChange = { gatheringTime = it }
        )
        TimePicker(state =)*/

        Button(onClick = {
            viewModel.createEvent(
                eventName,
                selectedGame!!.id,
                location,
                description,
                playerCount,
                gatheringTime
            )
        }) {
            Text("Создать мероприятие")
        }
    }
}
/*
@Preview
@Composable
fun EventDetailsScreenPreview(){
    //EventDetailsScreen(eventId = 0)
}*/
