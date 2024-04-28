package com.example.duno.compose.events

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    eventId: Int,
    //navController: NavController,
    //meetingViewModel: MeetingViewModel = hiltViewModel()
) {
    //var event = meetingViewModel.meetingList.observeAsState().value
    val event = Eventt()
    Text(modifier = Modifier.fillMaxSize(), text = "ee")
    if (event == null) {
        Text(modifier = Modifier.fillMaxSize(), text = "???")
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text(event.events[eventId].meetingGame)},
                    navigationIcon = {
                        IconButton(onClick = {
                            //navController.popBackStack()
                            }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    }
                )
            },
            containerColor = Colors.ss_BackGround,
            content = {
                paddingValues ->
                    Column(
                        modifier = Modifier.padding(top = paddingValues.calculateTopPadding()).fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        EventStatusChip(event.events[eventId].meetingStatus)
                        EventInfoCard(event.events[eventId])
                        EventOrganizer(event.events[eventId].meetingOrganizer)
                        EventDescription(event.events[eventId].meetingBody)
                        EventMapp(event.events[eventId].meetingGame)
                        EventDateTime(
                            event.events[eventId].meetingClosed.toString(),
                            event.events[eventId].meetingCreated.toString())
                        EventActions(event.events[eventId].meetingId.toString())
                    }
            }
        )
    }
}

private class Eventt{
    val event = ApiMeeting.Ilya
    val events = listOf(event)
}

@Composable
fun EventStatusChip(status: Boolean) {
    val color = when (status) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.secondary
        //EventStatus.CANCELLED -> MaterialTheme.colorScheme.error
    }

}

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
fun EventInfoCard(event: ApiMeeting.Ilya) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = DunoSizes.tinyDp,
                vertical = DunoSizes.tinyDp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.ss_MainColor,
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = event.meetingTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = Colors.ss_BackGround
            )
            Divider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp), color = Colors.md_PrimaryContainer)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = DunoSizes.tinyDp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Жанр", style = MaterialTheme.typography.titleMedium, color = Colors.ss_BackGround)
                Text(text = event.meetingGenre, style = MaterialTheme.typography.titleMedium, color = Colors.ss_BackGround)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Кол-во игроков", style = MaterialTheme.typography.titleMedium, color = Colors.ss_BackGround)
                Text(text = event.meetingCountPlayers.toString(), style = MaterialTheme.typography.titleMedium, color = Colors.ss_BackGround)
            }
        }
    }
}

@Composable
fun EventDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        color = Colors.ss_MainColor
    )
}

@Composable
fun EventOrganizer(organizer: String) {
    Text(
        text = "Организатор: $organizer",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        color = Colors.ss_MainColor
    )
}

@Preview
@Composable
fun EventDetailsScreenPreview(){
    EventDetailsScreen(eventId = 0)
}