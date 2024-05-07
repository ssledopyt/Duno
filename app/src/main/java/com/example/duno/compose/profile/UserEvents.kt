package com.example.duno.compose.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duno.compose.elements.searchingTitleGeo
import com.example.duno.db.ApiLocationOfSP
import com.example.duno.viewmodel.DunoEventUIState
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.DunoSizes
import timber.log.Timber
import com.example.duno.ui.Colors
import com.example.duno.viewmodel.MeetingViewModel
import com.example.duno.viewmodel.UserViewModel
import java.time.LocalDateTime


private var viewUserEvents = mutableStateOf("Мои мероприятия")
private var getLikes = mutableStateOf(false)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEventsProfile(
    title:String,
    userNickname: String,
    meetingViewModel:MeetingViewModel,
    userViewModel: UserViewModel,
    navigateToProfile: () -> Unit,
    ) {
    viewUserEvents.value = title
    BackHandler {
        navigateToProfile()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            //       .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "UserEventsProfile" }
            .background(color = Colors.es_Background),

    ) {
        val userUIState = userViewModel.places.observeAsState()
        Box(modifier = Modifier.fillMaxWidth()){
            /*Button(onClick = { *//*TODO*//* },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Colors.es_Background,
                    contentColor = Colors.ss_AccentColor
                    )) {
                Icon(imageVector = Icons.Filled.KeyboardArrowLeft,contentDescription = null)
            }*/
            Text(title,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium)
        }
        Box(modifier = Modifier
            .padding(top = DunoSizes.tinyDp)
            .padding(horizontal = DunoSizes.tinyDp)){
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            //val meetingViewModel: MeetingViewModelPreview = MeetingViewModelPreview()
            val meetingUIState by meetingViewModel.meetingList.observeAsState()
            LaunchedEffect(!getLikes.value){
                meetingViewModel.getUserLikes(userNickname)
                getLikes.value = true
            }
            //Timber.e(meetingUIState?.events.toString())
            if (meetingUIState?.events.isNullOrEmpty()){
                Text(modifier = Modifier.fillMaxSize(), text = "internet?")
            }
            else {
                EventsList(listState, meetingUIState, userUIState.value, userNickname)
            }
            /*FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp),
                onClick = {
                    coroutineScope.launch{ listState.animateScrollToItem(index = 0)  }
                },
                containerColor = Colors.es_PrimaryCard
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowUp,contentDescription = null)
            }*/
        }
    }
}


@Composable
fun EventsList(
    listState: LazyListState,
    meetingUIState: DunoEventUIState?,
    places: List<ApiLocationOfSP>?,
    userNickname: String
) {
    var userEvents by remember{ mutableStateOf(false) }
    var localDTime = LocalDateTime.now()
    Timber.e("Second")
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Timber.e("UserEvents")
        when (viewUserEvents.value){
            "Мои мероприятия" -> {
                itemsIndexed(meetingUIState!!.events) {index, event ->
                    val eventTime = LocalDateTime.parse(event.meetingDate)
                    if (event.meetingOrganizer == userNickname && eventTime.isAfter(localDTime)) {
                        EventsDetails(event = event, eventTime = eventTime, places = places)
                        userEvents = true
                    }
                    else if(index == meetingUIState.events.size){
                        Text("У вас нет ваших мероприятий. Сначала создайте своё!")
                    }
                }
            }
            "Избранное" ->{
                if (meetingUIState!!.favEvents.isEmpty()) {
                    items(1) {
                        Text("У вас нет избранных мероприятий. Добавьте его, чтобы начать следить за встречами")
                    }
                }else{
                    items(meetingUIState.events) {event ->
                        val eventTime = LocalDateTime.parse(event.meetingDate)
                        if (meetingUIState.favEvents.contains(event.meetingId))
                            EventsDetails(event = event, eventTime = eventTime, places = places)
                    }
                }
            }
            "Архив событий" -> {
                itemsIndexed(meetingUIState!!.events) {index, event ->
                    val eventTime = LocalDateTime.parse(event.meetingDate)
                    if (event.meetingOrganizer == userNickname && eventTime.isBefore(localDTime))
                        EventsDetails(event = event, eventTime = eventTime, places = places)
                    else if(index == meetingUIState.events.size){
                        Text("У вас нет ваших мероприятий. Сначала создайте своё!")
                    }
                }
            }
        }
    }
}


@Composable
fun EventsDetails(
    modifier: Modifier = Modifier,
    event: ApiMeeting,
    eventTime: LocalDateTime,
    places: List<ApiLocationOfSP>?
    //onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Colors.ss_BackGround,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Изображение игры
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.meetingTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = event.clubName,
                            //style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Light,
                            maxLines = 1,

                            )
                        Text(
                            text = "${eventTime.dayOfMonth}.${eventTime.monthValue}.${eventTime.year} " +
                                    "в ${eventTime.hour}:${eventTime.minute}",
                            //style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Light,
                            maxLines = 1,
                        )
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.meetingBody,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Normal,
                maxLines = 2
            )
        }
    }
}



@Preview
@Composable
fun UserEventsProfilePreview(){
    //UserEventsProfile("Мои мероприятия", "tah")
}