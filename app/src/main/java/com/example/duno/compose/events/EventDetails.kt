package com.example.duno.compose.events

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes
import com.example.duno.viewmodel.MeetingViewModel
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    eventId: Int,
    //navController: NavController,
    meetingViewModel: MeetingViewModel
) {
    var event = meetingViewModel.meetingList.value
    var hintShow = remember { mutableStateOf(true) }
    var showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()



    //var event = Eventt()
    Timber.e("EventsDetail")
    Text(modifier = Modifier.fillMaxSize(), text = "ee")
    if (event == null) {
        Text(modifier = Modifier.fillMaxSize(), text = "???")
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(48.dp),
                    title = { Box(Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = "О мероприятии")
                    }},
                    navigationIcon = {
                        IconButton(onClick = {
                            //navController.popBackStack()
                            }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    },
                )
            },
            containerColor = Colors.ss_BackGround,
            content = {
                paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(top = paddingValues.calculateTopPadding())
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        EventTitle(event.events[eventId].meetingTitle)
                        EventClub(event.events[eventId].clubName)
                        EventStatusChip(event.events[eventId].meetingStatus)
                        EventDateTime(
                            event.events[eventId].meetingClosed.toString(),
                            event.events[eventId].meetingDate.toString())
                        EventHint(hintShow)
                        EventDescription(event.events[eventId].meetingOrganizer, event.events[eventId].meetingBody)
                        EventInfoCard(event.events[eventId], showBottomSheet)
                        //EventMapScreen(event.events[eventId].meetingGame)
                        //EventActions(event.events[eventId].meetingId.toString())
                        if (showBottomSheet.value) {
                            InfoBottomSheet(
                                showBottomSheet = showBottomSheet,
                                scope,
                                sheetState,
                                event.events[eventId]
                            )
                        }
                    }
            }
        )
    }
}

@Composable
fun EventOrganizer(organizer:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = "Организатор: ",
            style = MaterialTheme.typography.bodyMedium,
            color = Colors.ss_MainColor
        )
        Text(
            text = organizer,
            style = MaterialTheme.typography.bodyMedium,
            color = Colors.ss_MainColor
        )
    }
}


@Composable
fun EventTitle(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, 8.dp),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EventClub(club: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp),
            text = club,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun EventHint(hintShow: MutableState<Boolean>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Описание",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        AnimatedVisibility(visible = hintShow.value) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = DunoSizes.superTinyDp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Colors.es_AttentionCard,
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                ) {
                    //Text(text = "Подсказка")
                    Text(
                        text = "Помните, переходите только по знакомым ссылкам:" +
                                " Вконтакте, Телеграм, сайты клубов и антикафе.",
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(width = 200.dp, height = 35.dp),
                        border = null,
                        onClick = { hintShow.value = false }) {
                        Text(
                            text = "Хорошо, я всё понял!",
                            color = Colors.md_Background
                        )
                    }
                }
            }
        }
    }
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
fun EventDateTime(closedAt: String, startsAt: String) {
    val closedAtDate = LocalDateTime.parse(closedAt)
    val startsAtDate = LocalDateTime.parse(startsAt)
    Column (Modifier.fillMaxWidth().padding(16.dp)){
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Дата проведения",
                style = MaterialTheme.typography.titleMedium,
                //modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${startsAtDate.dayOfMonth}.${startsAtDate.monthValue}.${startsAtDate.year} " +
                        "в ${startsAtDate.hour}:${startsAtDate.minute}",
                style = MaterialTheme.typography.bodyMedium,
                //modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
            )
        }
    }
}


@Composable
fun EventMapScreen(location: String) {
    // TODO: Implement map with location
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Карта недоступна")
    }
}


@Composable
fun EventInfoCard(event: ApiMeeting, showBottomSheet: MutableState<Boolean>) {
    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .height(54.dp)
                .padding(
                    horizontal = 16.dp,
                    vertical = DunoSizes.superTinyDp
                )
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(
                //containerColor = Colors.md_Background,
            ),
            onClick = { showBottomSheet.value = true }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Colors.ss_AccentColor)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Сведения о настольной игре",
                    style = MaterialTheme.typography.titleSmall,
                    color = Colors.ss_BackGround,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    scope: CoroutineScope,
    sheetState: SheetState,
    event: ApiMeeting
){
    ModalBottomSheet(
        modifier = Modifier.padding(bottom = 20.dp),
        onDismissRequest = { showBottomSheet.value = false },
        containerColor = Color.White) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = event.meetingGame,
                style = MaterialTheme.typography.titleMedium,
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                color = Colors.md_PrimaryContainer
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = DunoSizes.tinyDp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Жанр",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = event.meetingGenre,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = DunoSizes.tinyDp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Кол-во игроков",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = event.meetingCountPlayers.toString(),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
@Composable
fun EventDescription(organizer: String, description: String) {
    EventOrganizer(organizer = organizer)
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp),
        color = Colors.ss_MainColor
    )
}


private class Eventt{
    val events = listOf(ApiMeeting.Ilya)
}

@Preview
@Composable
fun EventDetailsScreenPreview(){
   // EventDetailsScreen(eventId = 0)
}
