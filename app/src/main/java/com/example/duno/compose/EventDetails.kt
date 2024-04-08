package com.example.duno.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.duno.data.MeetingViewModel

/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    eventId: String,
    navController: NavController,
    viewModel: MeetingViewModel = hiltViewModel()
) {
    val event by viewModel.meetingList.observeAsState()
    Text(modifier = Modifier.fillMaxSize(), text = "ee")
    if (event == null) {
        Text(modifier = Modifier.fillMaxSize(), text = "ee")
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(event.va) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                ) {
                    EventStatusChip(event.status)
                    EventInfoCard(event)
                    EventDescription(event.description)
                    EventOrganizer(event.organizer)
                    EventMapp(event.location)
                    EventDateTime(event.createdAt, event.startsAt)
                    EventActions(event.id)
                }
            }
        )
    }
}

@Composable
fun EventStatusChip(status: EventStatus) {
    val color = when (status) {
        EventStatus.ACTIVE -> MaterialTheme.colorScheme.primary
        EventStatus.COMPLETED -> MaterialTheme.colorScheme.secondary
        EventStatus.CANCELLED -> MaterialTheme.colorScheme.error
    }
}
*/

@Composable
fun EventActions(eventId: String) {
    // TODO: Implement actions depending on user role and event status
    // For example: join event, leave event, edit event, delete event

    Button(
        onClick = { /* TODO: Implement action */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Зарегистрироваться")
    }
}
@Composable
fun EventDateTime(createdAt: String, startsAt: String) {
    Text(
        text = "Дата и время:",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
    Text(
        text = "Создано: $createdAt\nНачнется: $startsAt",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(16.dp)
    )
}


@Composable
fun EventMapp(location: String) {
    // TODO: Implement map with location

    // You can use Google Maps Compose library for this: https://developer.android.com/jetpack/androidx/compose/material/maps

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Карта недоступна")
    }
}


@Composable
fun EventInfoCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Информация о мероприятии",
                style = MaterialTheme.typography.headlineSmall,
            )
            Divider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Жанр игры:", style = MaterialTheme.typography.labelLarge)
                Text(text = "Alloha", style = MaterialTheme.typography.labelLarge)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Количество участников:", style = MaterialTheme.typography.labelLarge)
                Text(text = "Ooppo", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun EventDescription(description: String) {
    Text(
        text = "Описание:",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
    Text(
        text = description,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun EventOrganizer(organizer: String) {
    Text(
        text = "Организатор:",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
    Text(
        text = organizer,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(16.dp)
    )
}

