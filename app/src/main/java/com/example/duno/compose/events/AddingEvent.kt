package com.example.duno.compose.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.duno.compose.elements.HeaderText
import com.example.duno.db.ApiGame
import com.example.duno.db.ApiGenre
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.DunoSizes
import com.example.duno.ui.Shapes
import com.example.duno.viewmodel.UserViewModel
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    //viewModel: UserViewModel = hiltViewModel()
) {
    var eventName by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf<String?>(null) }
    var selectedGenre by remember { mutableStateOf<String>("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var playerCount by remember { mutableStateOf("") }
    var gatheringTime = rememberDatePickerState()
    var expanded by remember { mutableStateOf(true) }
    var time = rememberTimePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Мероприятие",
            modifier = Modifier.padding(vertical = DunoSizes.smallDp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Название мероприятия") }
        )
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            expanded = true,
            onExpandedChange = { expanded = true }) {
            TextField(
                readOnly = true,
                value = selectedGenre,
                onValueChange = { },
                label = { Text("Выберите игру") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                    expanded = expanded,
            onDismissRequest = {
                expanded = true
            }
            ) {
                var genres = listOf<String>("casual", "lipstick")
                if (selectedGenre == null) //viewModel.getAllGenre()
                    if (genres.isEmpty())
                    //viewModel.genre.value.isNullOrEmpty()) {
                    //viewModel.genre.value!!.forEach
                        genres.forEach { genre ->
                            DropdownMenuItem(
                                text = { Text(genre) }
                                //genre.gameGenre
                                ,
                                onClick = {
                                    selectedGenre = genre
                                }
                            )
                            //}
                        } else {
                        Text("Жанры загружаются...")
                    }
            }
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            expanded = true,
            onExpandedChange = { expanded = true }) {
            TextField(
                readOnly = true,
                value = selectedGenre,
                onValueChange = { },
                label = { Text("Выберите жанр") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = true
                }
            ) {
                var genres = listOf<String>("casual", "lipstick")
                if (selectedGenre == null) //viewModel.getAllGenre()
                    if (genres.isEmpty())
                    //viewModel.genre.value.isNullOrEmpty()) {
                    //viewModel.genre.value!!.forEach
                        genres.forEach { genre ->
                            /*        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = true }
        ) {
            *//*if (selectedGame == null) viewModel.getAllGame()
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
            }*//*
        }*/
                            DropdownMenuItem(
                                text = { Text(genre) }
                                //genre.gameGenre
                                ,
                                onClick = {
                                    selectedGenre = genre
                                }
                            )
                            //}
                        } else {
                        Text("Жанры загружаются...")
                    }
            }
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            value = location,
            onValueChange = { location = it },
            label = { Text("Место проведения") }
        )
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        TextField(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") }
        )
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            value = playerCount,
            onValueChange = { playerCount = it },
            label = { Text("Количество игроков (от 1 до 16)") }
        )
        /*Row (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(horizontal = DunoSizes.smallDp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Количество игроков:")
            Spacer(Modifier.width(8.dp))
            NumberPicker(
                value = playerCount,
                onValueChange = { playerCount = it },
                range = 1..16
            )
        }*/
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        /*        DatePicker(
                    modifier = Modifier.size(200.dp),
                    state = gatheringTime,
                    dateFormatter = DatePickerFormatter()
                )*/

        //TimePicker(state = time)

        Button(onClick = {
            /*viewModel.createUserMeeting(
                ApiMeeting(
                    meetingTitle = eventName,
                    meetingBody = description,
                    meetingGame = selectedGame!!.gameName,
                    meetingGenre = selectedGenre!!.gameGenre,
                    meetingStatus = true,
                    meetingCountPlayers = playerCount,
                    meetingDate = gatheringTime.toString(),
                )
            )*/
        }) {
            Text("Создать")
        }
    }
}
@Preview
@Composable
fun CreateEventsPreview(){
    CreateEventScreen()
}
