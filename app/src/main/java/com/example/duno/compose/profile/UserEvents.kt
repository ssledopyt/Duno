package com.example.duno.compose.profile

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duno.data.DunoEventUIState
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.DunoSizes
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.duno.data.MeetingViewModelPreview
import com.example.duno.ui.Colors


private var viewUserEvents = mutableStateOf("Мои мероприятия")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEventsProfile(title:String) {
    viewUserEvents.value = title
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            //       .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "UserEventsProfile" }
            .background(color = Colors.es_Background),

    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Colors.es_Background,
                    contentColor = Colors.ss_AccentColor
                    )) {
                Icon(imageVector = Icons.Filled.KeyboardArrowLeft,contentDescription = null)
            }
            Text(title,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium)
        }
        Box(modifier = Modifier
            .padding(top = DunoSizes.tinyDp)
            .padding(horizontal = DunoSizes.tinyDp)){
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            //val meetingViewModel: MeetingViewModel = hiltViewModel()
            val meetingViewModel: MeetingViewModelPreview = MeetingViewModelPreview()
            var meetingUIState = meetingViewModel.meetingList
            if (meetingUIState?.events.isNullOrEmpty()){
                Text(modifier = Modifier.fillMaxSize(), text = "internet?")
            }
            else {
                EventsList(listState, meetingUIState)
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
    meetingUIState: DunoEventUIState?
) {
    Timber.e("Second")
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Timber.e("UserEvents")
        if (viewUserEvents.value == "Мои мероприятия"){
            items(meetingUIState!!.events) { event ->
                EventsDetails(event = event)
            }
        }
        else{
            items(meetingUIState!!.favEvents) { event ->
                //if event.nickname == TODO()

                //EventsDetails(event = event)
            }

        }
    }
}


@Composable
fun EventsDetails(
    modifier: Modifier = Modifier,
    event: ApiMeeting,
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
                    Text(
                        text = "${event.meetingGeoMarker}, ${event.meetingDate}",
                        //style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,

                    )
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
    UserEventsProfile("Мои мероприятия")
}