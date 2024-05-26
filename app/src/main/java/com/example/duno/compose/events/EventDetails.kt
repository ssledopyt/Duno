package com.example.duno.compose.events

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.LiveData
import com.example.duno.compose.elements.TextInLineWithSpace
import com.example.duno.compose.elements.TextInTwoLines
import com.example.duno.compose.elements.get_correct_dateTime
import com.example.duno.compose.elements.get_month
import com.example.duno.db.ApiGame
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes
import com.example.duno.viewmodel.MeetingViewModel
import com.example.duno.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EventDetailsScreen(
    eventId: Int,
    onBack: () -> Unit,
    meetingViewModel: MeetingViewModel,
    userViewModel: UserViewModel,
    showButtonDeleteLike: Boolean,
    userNickname: String,
    innerPadding: PaddingValues,
    userName: String
) {
    val events = meetingViewModel.meetingList.value
    var hintShow = remember { mutableStateOf(true) }
    var showBottomSheet = remember { mutableStateOf(false) }
    var redactionShow = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val games = userViewModel.games.observeAsState()
    BackHandler {
        onBack()
    }
    //val event = ApiMeeting.Ilya
    Timber.e("EventsDetail")
    Text(modifier = Modifier.fillMaxSize(), text = "ee")
    if (events == null) {
        Text(modifier = Modifier.fillMaxSize(), text = "???")
    } else {
        val event = events.events.find { it.meetingId == eventId }!!
        var redactionText by remember { mutableStateOf(event.meetingBody) }
        var radioStatus by remember { mutableStateOf(event.meetingStatus) }

        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(48.dp),
                    title = { Box(Modifier.fillMaxSize()) {
                        /*Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = "О мероприятии")*/
                    }},
                    navigationIcon = {
                        IconButton(onClick = {
                            onBack()
                            }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                    ),
                )
            },
            containerColor = Color.White,
            content = {
                paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(top = paddingValues.calculateTopPadding())
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Timber.e(event.toString())
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)) {
                            EventTitle(event.meetingTitle, redactionShow, event.meetingOrganizer==userNickname)
                            EventClub(event.clubName)
                            EventInfoCard(event, showBottomSheet)
                        }
                        Column(
                            Modifier
                                .height(20.dp)
                                .background(Color("#f5f3f4".toColorInt()))) {
                            Spacer(modifier = Modifier.fillMaxSize())
                        }

                        Column(modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)) {
                            EventStatusChip(radioStatus)
                            EventDateTime(
                                event.meetingDate)
                            EventHint(hintShow)
                            Column (Modifier.fillMaxSize()) {
                                if (redactionShow.value) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            text = "Статус мероприятия: ",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Colors.ss_MainColor
                                        )
                                        RadioButton(
                                            modifier = Modifier
                                                .padding(horizontal = DunoSizes.smallDp)
                                                .align(Alignment.CenterVertically),
                                            selected = radioStatus,
                                            onClick = { radioStatus = !radioStatus },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color.Green,
                                                unselectedColor = Color.Red
                                            )
                                        )
                                    }
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = DunoSizes.smallDp),
                                        value = redactionText,
                                        onValueChange = { redactionText = it },
                                        label = { Text("Изменить описание") },
                                        colors = TextFieldDefaults.colors(focusedContainerColor = Colors.ss_BackGround)
                                    )
                                    //кнопка для принятия изменений
                                    Button(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Colors.md_PrimaryContainer),
                                        onClick = {
                                            meetingViewModel.updateOrDelete(
                                                event,
                                                false,
                                                redactionText,
                                                radioStatus
                                            )
                                            userViewModel.updateUserMeeting(
                                                event.meetingId!!,
                                                redactionText,
                                                radioStatus
                                            )
                                            redactionShow.value = false
                                        }) {
                                        Text(
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            text = "Принять изменения",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Colors.ss_BackGround,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    EventDescription(
                                        Modifier.fillMaxSize(),
                                        event.meetingCountPlayers.toString(),
                                        userName,
                                        event.meetingBody
                                    )
                                }
                            }
                        }
                        //fun Modifier.myModifier() =this.fillMaxSize()

/*                        if (showButtonDeleteLike) {
                            EventDeleteLike(userViewModel)
                        }*/
                        //EventMapScreen(event.events[eventId].meetingGame)
                        //EventActions(event.events[eventId].meetingId.toString())
                        if (showBottomSheet.value) {
                            InfoBottomSheet(
                                userViewModel,
                                showBottomSheet = showBottomSheet,
                                scope,
                                sheetState,
                                event,
                                games.value,
                                innerPadding
                            )
                        }
                    }
            }
        )
    }
}


@Composable
fun EventCountPlayers(countPlayers:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = "Кол-во игроков: ",
            style = MaterialTheme.typography.bodyMedium,
            color = Colors.ss_MainColor
        )
        Text(
            text = countPlayers,
            style = MaterialTheme.typography.bodyMedium,
            color = Colors.ss_MainColor
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
fun EventTitle(title: String, redactionShow:MutableState<Boolean>, showIcon:Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row (
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                 //   .padding(start = 16.dp, 8.dp),
                ,text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            if (showIcon) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    onClick = {
                        redactionShow.value = !redactionShow.value }) {
                    Icon(modifier = Modifier.size(24.dp),
                        imageVector = Icons.Filled.Create, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun EventClub(club: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp),
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
        true -> Colors.es_Accept
        false -> Colors.es_Cancel
        //EventStatus.CANCELLED -> MaterialTheme.colorScheme.error
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
        .background(color)) {
    }

}

@Composable
fun EventDateTime(startsAt: String) {
    val startsAtDate = LocalDateTime.parse(startsAt)
    Column (
        Modifier
            .fillMaxWidth()
            .padding(16.dp)){
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Дата проведения",
                style = MaterialTheme.typography.titleMedium,
                //modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${get_correct_dateTime(startsAtDate.dayOfMonth)} ${get_month(startsAtDate.monthValue)} " +
                        "в ${get_correct_dateTime(startsAtDate.hour)}:${get_correct_dateTime(startsAtDate.minute)}",
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
    Box() {
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
    userViewModel: UserViewModel,
    showBottomSheet: MutableState<Boolean>,
    scope: CoroutineScope,
    sheetState: SheetState,
    event: ApiMeeting,
    gamesState: List<ApiGame>?,
    innerPadding: PaddingValues,
    ): State<LiveData<List<ApiGame>?>> {
    ModalBottomSheet(
        sheetState = sheetState,
        windowInsets = WindowInsets(bottom = innerPadding.calculateBottomPadding()),
        modifier = Modifier.padding(bottom = 20.dp),
        onDismissRequest = { showBottomSheet.value = false },
        containerColor = Color.White,
    ) {
        LaunchedEffect(sheetState.hasPartiallyExpandedState){
            sheetState.show()
        }
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Timber.e(gamesState.toString())
            if (gamesState.isNullOrEmpty()){
                Text(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally), text = "Подождите, данные уже в пути!")
            }
            else{
                Timber.e(gamesState.toString())
                val gameInfo = gamesState.find { it.gameName == event.meetingGame}
                Text(
                    text = gameInfo!!.gameName,
                    style = MaterialTheme.typography.titleMedium,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    color = Colors.md_PrimaryContainer
                )
                TextInLineWithSpace(leftText = "Жанр:",rightText = gameInfo.gameGenre)
                TextInLineWithSpace(leftText = "Расчитана на:", rightText = if (gameInfo.gameCountPlayers==0){"Информация отсутствует"} else {gameInfo.gameCountPlayers.toString()})
                TextInTwoLines(leftText = "Краткое описание:",rightText = gameInfo.gameDescription)
                TextInLineWithSpace(leftText = "Примерное время игры:",rightText = "${gameInfo.gameTime} ч.")
            }
        }
    }
    return produceState(initialValue = userViewModel.games, gamesState){
        if (gamesState.isNullOrEmpty()){
            userViewModel.getAllGame()
        }
        Timber.e(userViewModel.games.value.toString())
        value = userViewModel.games
    }
}


@Composable
fun EventDescription(modifier: Modifier,countPlayers: String,organizer: String, description: String) {
    Column (modifier = modifier
        .fillMaxSize()
        .padding(bottom = 12.dp)){
        EventCountPlayers(countPlayers)
        EventOrganizer(organizer = organizer)
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Colors.ss_MainColor
        )
    }

}


@Preview
@Composable
fun EventDetailsScreenPreview(){
   // EventDetailsScreen(eventId = 0)
}
