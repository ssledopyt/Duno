package com.example.duno.compose.events

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.duno.compose.elements.EventsCard
import com.example.duno.compose.elements.TwiceBackHandler
import com.example.duno.db.ApiLocationOfSP
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.DunoSizes
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.duno.ui.Colors
import com.example.duno.viewmodel.DunoEventUIState
import com.example.duno.viewmodel.MeetingViewModel
import com.example.duno.viewmodel.UserViewModel
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EventsScreen(
    goToEventDetails: (Int, Boolean) -> Unit,
    userNickname: String,
    meetingViewModel: MeetingViewModel,
    userViewModel: UserViewModel,
) {
    //var eventId = remember{ mutableIntStateOf(0) }
    var click = remember { mutableStateOf(false)}

    var meetings = remember { mutableListOf<ApiMeeting?>(null) }
    var places = remember { mutableListOf<ApiLocationOfSP?>(null) }
    var likes = remember { mutableListOf<Int?>(null) }

    val meetingUIState by meetingViewModel.meetingList.observeAsState()
    val userUIState by userViewModel.places.observeAsState()

    var refreshing by remember { mutableStateOf(false) }


    Timber.e("EventsScreen")
    Timber.e(meetingViewModel.meetingList.value!!.favEvents.toString())

    val coroutineScope = rememberCoroutineScope()
    fun refresh() = coroutineScope.launch {
        refreshing = true
        meetingViewModel.getAllMeetings()
        meetingViewModel.getUserLikes(userNickname)
        refreshing = false
    }
    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

    var launch by remember { mutableStateOf(true) }
    //val lifecycleState by lifecycleOwner.lifecycle.currentState
    //val meetingUIState = meetingViewModel.meetingList.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Events Screen" }
            .background(color = Colors.es_Background)
    ) {
        val context = LocalContext.current
        val activity = (context as? Activity)
        TwiceBackHandler(onFirstBack = {
            Toast.makeText(
                context,
                "Нажмите снова, чтобы выйти",
                Toast.LENGTH_SHORT
            ).show()
        }) {
            activity!!.finish()
        }
        var text by remember { mutableStateOf("") } // Query for SearchBar
        var active by remember { mutableStateOf(false) }
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .height(76.dp)){
        SearchBar(modifier = Modifier
            .fillMaxWidth(1f)
            .align(Alignment.TopCenter)
            .height(50.dp)
            .offset(y = 6.dp)
            .padding(horizontal = DunoSizes.tinyDp),
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                if(!active){
                Text(text = "Поиск мероприятия",
                    fontSize = 14.sp)
                }
            },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            },
            colors = SearchBarDefaults.colors(
                containerColor = Color("#fefeff".toColorInt()),
                dividerColor = Colors.ss_AccentColor
                )
        ) {}
        }

/*        Box(modifier = Modifier.padding(top = DunoSizes.tinyDp, start = DunoSizes.smallDp)){
            PreviewFilterChipGroup()
        }*/
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = DunoSizes.tinyDp)
            .pullRefresh(pullRefreshState)
        ){
            val listState = rememberLazyListState()
            if (meetingUIState?.events.isNullOrEmpty())
            {
                Column (Modifier.align(Alignment.Center)){
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        text = "Проверьте подключение к интернету",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold)
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            refresh()
                        },
                        colors = ButtonDefaults.buttonColors(Colors.ss_AccentColor)
                    ) {
                        Text(text = "Обновить")
                    }
                }
            }else{
                EventsList(
                    listState,
                    meetingUIState,
                    meetingViewModel,
                    meetingUIState!!.events,
                    goToEventDetails = goToEventDetails,
                    //eventId,
                    meetingUIState!!.favEvents,
                    userNickname,
                    )
            }
            if(listState.canScrollBackward) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(15.dp)
                        .size(36.dp),
                    onClick = {
                        coroutineScope.launch{ listState.animateScrollToItem(index = 0)  }
                    },
                    containerColor = Colors.es_PrimaryCard
                ) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowUp,contentDescription = null)
                }
            }
            PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}




@Composable
fun EventsList(
    listState: LazyListState,
    meetingUIState: DunoEventUIState?,
    meetingViewModel: MeetingViewModel,
    events: List<ApiMeeting>,
    goToEventDetails: (Int, Boolean) -> Unit,
    userLikes: List<Int>,
    userNickname: String,
): State<List<Int>> {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Timber.e("EventList")
        Timber.e(userLikes.toString())
        itemsIndexed(events) { index, event ->
            //Timber.e((meetingUIState.favEvents?.meetingId?.contains(event.meetingId)).toString())
            if (event.meetingOrganizer != userNickname &&
                LocalDateTime.parse(event.meetingDate).isAfter(LocalDateTime.now())
            ) {
                EventsCard(
                    meetingViewModel = meetingViewModel,
                    userNickname = userNickname,
                    event = event,
                    userLike = userLikes.contains(event.meetingId),
                    goToEventDetails = goToEventDetails,
                    userLikes = userLikes,
                )
            }
        }
    }
    return produceState(initialValue = meetingUIState!!.favEvents, userNickname, meetingUIState.favEvents){
        //meetingViewModel.getUserLikes(userNickname)
        value = if (meetingUIState.favEvents.isEmpty()){
            emptyList()
        }else{
            Timber.e("Recomposition with ${meetingUIState.favEvents}")
            meetingUIState.favEvents
        }
    }
}


@Composable
fun IconStatus(
    status: Boolean
){
    if (status){
        FilterChip(modifier = Modifier.size(10.dp), selected = true, onClick = {  }, label = {  }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Colors.es_Accept))
        //Icon(imageVector = ImageVector.vectorResource(id = R.drawable.green_signal), contentDescription = null, tint= Color.Unspecified)
    }else{
        FilterChip(modifier = Modifier.size(10.dp), selected = true, onClick = {  }, label = {  }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Colors.es_Cancel))
        //Icon(imageVector = ImageVector.vectorResource(R.drawable.red_signal), contentDescription = null, tint= Color.Unspecified)
    }
}

@Composable
fun IconLike(
    userLikeEvent: MutableState<Boolean>,
    meetingViewModel: MeetingViewModel,
    userNickname: String,
    event: ApiMeeting
) {
    IconButton(
        modifier = Modifier.size(36.dp),
        onClick = {
            userLikeEvent.value = if (!userLikeEvent.value){
                meetingViewModel.putUserLikes(userNickname, event.meetingId)
                true
            }else{
                meetingViewModel.deleteUserLikes(userNickname, event.meetingId)
                false
            }
            //Timber.e(userLikes.toString())
        }) {
        if (userLikeEvent.value){
            Icon(imageVector = Icons.Filled.Favorite, contentDescription = null, tint = Color.Red)
        }
        else{
            Icon(imageVector = Icons.Sharp.FavoriteBorder, contentDescription = null, tint = Color.LightGray)

        }
    }
}


@Preview
@Composable
fun EventsScreenPreview(){
    //EventsScreen()
}

//CustomFilterChip
/*Box(modifier = Modifier.padding( start = 12.dp)) {
            var selected by remember { mutableStateOf(false) }
            var text by rememberSaveable { mutableStateOf("") }
            *//*TextField(
                modifier = Modifier.size(140.dp,40.dp),
                value = text,
                onValueChange = { text = it },
                label = { Text("Label") },
                singleLine = true
            )*//*
            FilterChip(
                modifier = Modifier.padding( end = 0.dp),
                onClick = {
                    selected = !selected
                    Timber.d("DnD filter")
                          },
                label = { Text("DnD") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Localized description",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                selected = selected,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Colors.md_Primary,
                    iconColor = Colors.md_Background,
                )
            )
        }*/