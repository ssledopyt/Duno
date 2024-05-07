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
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.duno.R
import com.example.duno.compose.elements.TwiceBackHandler
import com.example.duno.compose.elements.searchingTitleGeo
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
    goToEventDetails: (Int) -> Unit,
    userNickname: String,
    meetingViewModel: MeetingViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
) {
    var eventId = remember{ mutableIntStateOf(0) }
    var click = remember { mutableStateOf(false)}

    var meetings = remember { mutableListOf<ApiMeeting?>(null) }
    var places = remember { mutableListOf<ApiLocationOfSP?>(null) }
    var likes = remember { mutableListOf<Int?>(null) }

    val meetingUIState by meetingViewModel.meetingList.observeAsState()
    val userUIState by userViewModel.places.observeAsState()
    Timber.e("EventsScreen")
    Timber.e(meetingViewModel.meetingList.value!!.favEvents.toString())

    val coroutineScope = rememberCoroutineScope()
    val pullRef = rememberPullRefreshState(false, {meetingViewModel.getAllMeetings()})
    var launch by remember { mutableStateOf(true) }
    //val lifecycleState by lifecycleOwner.lifecycle.currentState
    //val meetingUIState = meetingViewModel.meetingList.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            //       .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Events Screen" }
            .background(color = Colors.es_Background)
            //.pullRefresh(pullRef),

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
            .background(color = Colors.md_Background)
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
                Text(text = "Поиск мероприятия",
                    fontSize = 14.sp)
            },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            },
            colors = SearchBarDefaults.colors(
                containerColor = Colors.ss_BackGround,
                dividerColor = Colors.ss_AccentColor
                )
        ) {}
        }

/*        Box(modifier = Modifier.padding(top = DunoSizes.tinyDp, start = DunoSizes.smallDp)){
            PreviewFilterChipGroup()
        }*/
        Box(modifier = Modifier
            .padding(top = DunoSizes.tinyDp)
            .padding(horizontal = DunoSizes.tinyDp)){
            val listState = rememberLazyListState()
            //val meetingViewModel: MeetingViewModelPreview = MeetingViewModelPreview()
            //var userLikes = remember {mutableListOf<Int>()}
            //Timber.e(meetingUIState?.toString())
/*            if (meetingUIState){
                Text(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                    text = "Загрузка мероприятий...")
            }
            else {*/
            EventsList(
                listState,
                meetingUIState,
                meetingViewModel,
                meetingUIState!!.events,
                goToEventDetails = {goToEventDetails(eventId.intValue)},
                eventId,
                meetingUIState!!.favEvents,
                userNickname,
                userUIState
                )
          //  }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp),
                onClick = {
                    coroutineScope.launch{ listState.animateScrollToItem(index = 0)  }
                },
                containerColor = Colors.es_PrimaryCard
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowUp,contentDescription = null)
            }
        }
    }
}


@Composable
fun EventsList(
    listState: LazyListState,
    meetingUIState: DunoEventUIState?,
    meetingViewModel: MeetingViewModel,
    events: List<ApiMeeting>,
    goToEventDetails: (Int) -> Unit,
    eventId: MutableIntState,
    userLikes: List<Int>,
    userNickname: String,
    places: List<ApiLocationOfSP>?
): State<List<Int>> {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Timber.e("EventList")
        Timber.e(userLikes.toString())
        itemsIndexed(events) { index, event ->
            //Timber.e((meetingUIState.favEvents?.meetingId?.contains(event.meetingId)).toString())
            if(event.meetingOrganizer != userNickname){
                EventsDetails(
                    meetingViewModel = meetingViewModel,
                    userNickname = userNickname,
                    event = event,
                    userLike = userLikes.contains(event.meetingId),
                    goToEventDetails = {goToEventDetails(eventId.intValue)},
                    userLikes = userLikes,
                    eventId = eventId,
                    index = index,
                    places = places
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsDetails(
    modifier: Modifier = Modifier,
    meetingViewModel: MeetingViewModel,
    userNickname: String,
    event: ApiMeeting,
    userLike: Boolean,
    goToEventDetails: (Int) -> Unit,
    userLikes: List<Int>,
    eventId: MutableIntState,
    index: Int,
    places: List<ApiLocationOfSP>?
) {
    var userLikeEvent by remember { mutableStateOf(userLike) }
    Timber.tag("UserLikesSET").e(userLikes.toString())
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Colors.ss_BackGround,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            eventId.intValue = index
            goToEventDetails(eventId.intValue)
        }
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
                    val startsAtDate = LocalDateTime.parse(event.meetingDate)
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier){
                            Text(
                                text = event.meetingTitle,
                                style = MaterialTheme.typography.headlineMedium,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold
                            )
                            if (startsAtDate.isAfter(LocalDateTime.now())){
                                Icon(painter = painterResource(id = R.drawable.icon_enable_event), contentDescription = null, tint = Color.LightGray)
                            }else{
                                Icon(painter = painterResource(id = R.drawable.icon_completed_event), contentDescription = null, tint = Color.Red)

                            }
                        }
                        IconButton(modifier = Modifier,
                            onClick = {
                            userLikeEvent = if (!userLikeEvent){
                                meetingViewModel.putUserLikes(userNickname, event.meetingId)
                                true
                            }else{
                                meetingViewModel.deleteUserLikes(userNickname, event.meetingId)
                                false
                            }
                                Timber.e(userLikes.toString())
                        }) {
                            if (userLikeEvent){
                                Icon(imageVector = Icons.Filled.Favorite, contentDescription = null, tint = Color.Red)
                            }
                            else{
                                Icon(imageVector = Icons.Sharp.FavoriteBorder, contentDescription = null, tint = Color.LightGray)

                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = event.clubName,
                            //style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Light,
                            maxLines = 1,

                            )
                        Text(
                            text = "${startsAtDate.dayOfMonth}.${startsAtDate.monthValue}.${startsAtDate.year} " +
                                    "в ${startsAtDate.hour}:${startsAtDate.minute}",
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
                text = event.meetingBody.ifEmpty { "Пока пусто" },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Normal,
                maxLines = 2
            )
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