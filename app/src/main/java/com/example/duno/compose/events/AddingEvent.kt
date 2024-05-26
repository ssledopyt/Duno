package com.example.duno.compose.events

import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.duno.compose.elements.HeaderText
import com.example.duno.db.ApiGame
import com.example.duno.db.ApiGenre
import com.example.duno.db.ApiLocationOfSP
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes
import com.example.duno.viewmodel.UserViewModel
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.ui.graphics.Color
import com.example.duno.compose.elements.get_correct_dateTime
import com.example.duno.compose.elements.get_month
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date


//@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    viewModel: UserViewModel,
    goToEvents: () -> Unit
) {
    var eventName by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf<String>("") }
    var selectedGenre by remember { mutableStateOf<String>("") }
    val selectedPlace = remember { mutableStateMapOf<String, List<Double>>("" to listOf<Double>(0.0, 0.0))}
    var description by remember { mutableStateOf("") }
    var playerCount by remember { mutableStateOf("") }

    val location = remember { mutableListOf<ApiLocationOfSP?>(null) }
    val genres = remember { mutableListOf<ApiGenre?>(null) }
    val games = remember { mutableListOf<ApiGame?>(null) }
    var expandedGame by remember { mutableStateOf(false) }
    var expandedPlace by remember { mutableStateOf(false) }
    var expandedGenre by remember { mutableStateOf(false) }

    var expandedTime by remember { mutableStateOf(false) }
    var expandedDate by remember { mutableStateOf(false) }

    var date by remember { mutableStateOf(LocalDate.now()) }
    val time = rememberTimePickerState()

    Timber.e("AddingScreen")
    var launch by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(launch){
        val getGames = viewModel.getAllGame()
        val getGenres = viewModel.getAllGenre()
        val getPlaces = viewModel.getAllPlaces()
        coroutineScope.launch {
            delay(3000)
            if (getGames.isCompleted && getGenres.isCompleted && getPlaces.isCompleted)
                genres.clear()
                games.clear()
                location.clear()
                viewModel.genre.value?.toCollection(genres)
                viewModel.games.value?.toCollection(games)
                viewModel.places.value?.toCollection(location)
                launch = false
                Timber.e("Add all to lists")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Ваше мероприятие",
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = eventName,
            onValueChange = { eventName = it },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Colors.es_CreatingTextField,
                disabledContainerColor = Colors.es_CreatingTextField,
                focusedContainerColor = Colors.es_CreatingTextField
            ),
            placeholder = {
                if (eventName=="") Text("Название мероприятия")
            },
            maxLines = 1,
        )
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        //GAME
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            expanded = expandedGame,
            onExpandedChange = { expandedGame = !expandedGame }) {
            TextField(
                readOnly = true,
                value = selectedGame,
                onValueChange = { },
                label = { Text("Выберите игру") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandedGame
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = Colors.es_CreatingTextField,
                    disabledContainerColor = Colors.es_CreatingTextField,
                    focusedContainerColor = Colors.es_CreatingTextField
                ),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedGame,
                onDismissRequest = {
                    expandedGame = false
                }
            ) {
                Timber.e("trying to load list1")
                if (games.isEmpty() || games[0]==null) {
                    if (!coroutineScope.coroutineContext.isActive){
                        launch = true
                    }
                    Text("Загрузка...")
                }
                else  {
                    games.forEach { game ->
                        //games.forEach
                        DropdownMenuItem(
                            text = {
                                    Text(game!!.gameName)
                            },
                            onClick = {
                                    selectedGame = game!!.gameName
                                    expandedGame = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        //GENRE
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            expanded = expandedGenre,
            onExpandedChange = { expandedGenre = !expandedGenre }) {
            TextField(
                readOnly = true,
                value = selectedGenre,
                onValueChange = { },
                label = { Text("Выберите жанр") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandedGenre
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = Colors.es_CreatingTextField,
                    disabledContainerColor = Colors.es_CreatingTextField,
                    focusedContainerColor = Colors.es_CreatingTextField
                ),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedGenre,
                onDismissRequest = {
                    expandedGenre = false
                }
            ) {
                Timber.e("trying to load list2")
                if (genres.isEmpty() || genres[0]==null) {
                    if (!coroutineScope.coroutineContext.isActive){
                        launch = true
                    }
                    Text("Загрузка...")
                }else {
                    genres.forEach { genre ->
                        DropdownMenuItem(
                            text = {
                                Text(genre!!.gameGenre)
                            },
                            onClick = {
                                selectedGenre = genre!!.gameGenre
                                expandedGenre = false
                            }
                        )
                    }
                }
                /*if (selectedGenre == null) viewModel.getAllGenre()
                if (!viewModel.genre.value.isNullOrEmpty()) {
                    //genres.isNotEmpty() {
                    viewModel.genre.value!!.forEach { genre ->
                        //genres.forEach
                        DropdownMenuItem(
                            text = { Text(genre.gameGenre) }
                            //genre.gameGenre
                            ,
                            onClick = {
                                selectedGenre = genre.gameGenre
                                expandedGenre = false
                            }
                        )
                    }
                } else {
                    Text("Ошибка :(")
                }*/
            }
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        //PLACE
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            expanded = expandedPlace,
            onExpandedChange = { expandedPlace = !expandedPlace }) {
            TextField(
                readOnly = true,
                value = selectedPlace.keys.first().toString(),
                onValueChange = { },
                label = { Text("Выберите место") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandedPlace
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = Colors.es_CreatingTextField,
                    disabledContainerColor = Colors.es_CreatingTextField,
                    focusedContainerColor = Colors.es_CreatingTextField
                ),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedPlace,
                onDismissRequest = {
                    expandedPlace = false
                }
            ) {
                Timber.e("trying to load list3")
                if (location.isEmpty() || location[0]==null) {
                    Text("Загрузка...")
                }else {
                    location.forEach { locate ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(text = locate!!.nameClub, style = MaterialTheme.typography.titleSmall)
                                    Text(text = locate.place, style = MaterialTheme.typography.bodySmall)
                                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth().padding(24.dp).align(Alignment.Start))
                                }
                            },
                            onClick = {
                                selectedPlace.clear()
                                selectedPlace[locate!!.nameClub] = locate.geoMarker
                                Timber.e(selectedPlace.keys.toString())
                                Timber.e(selectedPlace.toString())
                                expandedPlace = false
                            },
                        )
                    }
                }
                /*if (selectedPlace == null) viewModel.getAllPlaces()
                if (!viewModel.places.value.isNullOrEmpty()) {
                    //places.isNotEmpty() {
                    viewModel.places.value!!.forEach { place ->
                        //places.forEach
                        DropdownMenuItem(
                            text = { Text("${place.nameClub}: ${place.place}") }
                            //places.gameGenre
                            ,
                            onClick = {
                                selectedPlace[place.nameClub]=place.geoMarker
                                    //"${place.nameClub}: ${place.place}"
                                expandedPlace = false
                            }
                        )
                    }
                } else {
                    Text("Ошибка :(")
                }*/
            }
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        TextField(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = description,
            onValueChange = { description = it },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Colors.es_CreatingTextField,
                disabledContainerColor = Colors.es_CreatingTextField,
                focusedContainerColor = Colors.es_CreatingTextField
            ),
            placeholder = {
                if (description=="") Text("Описание")
            }
        )
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = playerCount,
            onValueChange = {
                when {
                    Regex("^[1-9]\$").matches(it) -> playerCount = it
                    Regex("^1[0-6]\$").matches(it) -> playerCount = it
                    Regex("").matches(it) -> playerCount = it
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Colors.es_CreatingTextField,
                disabledContainerColor = Colors.es_CreatingTextField,
                focusedContainerColor = Colors.es_CreatingTextField
            ),
            placeholder = {
                if (playerCount=="") Text("Количество игроков (от 1 до 16)")
                    },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start) {
            TextField(
                value = TextFieldValue(text = "${get_correct_dateTime(date.dayOfMonth)} ${get_month(date.monthValue+1)} ${date.year}"),
                onValueChange = {},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Colors.es_CreatingTextField,
                    disabledContainerColor = Colors.es_CreatingTextField,
                    focusedContainerColor = Colors.es_CreatingTextField
                ),
                modifier = Modifier
                    .width(180.dp)
                    .padding(end = 16.dp),
                label = {
                    Text(text = "Выберите дату")
                },
                readOnly = true)
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                ),
                onClick = { expandedDate=true }) {
                Text(text = "Выбрать", maxLines = 1, color = Color.Black )
            }
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start) {
            TextField(
                value = TextFieldValue(text ="${get_correct_dateTime(time.hour)}:${get_correct_dateTime(time.minute)}"),
                onValueChange = {},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Colors.es_CreatingTextField,
                    disabledContainerColor = Colors.es_CreatingTextField,
                    focusedContainerColor = Colors.es_CreatingTextField
                ),
                modifier = Modifier
                    .width(180.dp)
                    .padding(end = 16.dp),
                label = {
                    Text(text = "Выберите время")
                },
                readOnly = true)
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                ),
                onClick = { expandedTime=true }) {
                Text(text = "Выбрать", maxLines = 1, color = Color.Black)
            }
        }
        if (expandedDate){
            DatePickerDialog(
                modifier = Modifier.wrapContentSize(),
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.ss_AccentColor
                    ),
                        onClick = { expandedDate = false }
                    ) {
                    Text(text = "Назначить")}
                },
                colors = DatePickerDefaults.colors(
                        containerColor = Colors.es_Background,
                ),
                onDismissRequest = {expandedDate = false},
                content = {
                    //ShowDatePicker(date)
                    AndroidView(
                        { val cal = CalendarView(it)
                            cal.minDate = Calendar.getInstance().time.time
                            cal.date = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
                            cal},
                        modifier = Modifier.wrapContentWidth(),
                        update = { views ->
                            views.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                                date = LocalDate.now().withYear(year).withMonth(month+1).withDayOfMonth(dayOfMonth)
                            }
                        }
                    )
                    /*DatePicker(
                        state = date,
                        dateFormatter = DatePickerFormatter(selectedDateDescriptionSkeleton = "MMMM"),
                        dateValidator = {it<LocalTime.now().toNanoOfDay()})*/
                }
            )
        }

        if (expandedTime){
            Dialog(
                onDismissRequest = {expandedTime = false},
                properties = DialogProperties(),
                //dismissButton = {},
                content = {
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Colors.es_Background
                        )
                    ) {
                        Column(modifier = Modifier
                            .wrapContentSize()) {
                            TimePicker(state = time,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(16.dp),
                                colors = TimePickerDefaults.colors(
                                    selectorColor = Colors.ss_AccentColor,
                                    timeSelectorSelectedContainerColor = Colors.es_PrimaryCard,
                                    clockDialColor = Colors.es_AttentionCard,
                                    timeSelectorUnselectedContainerColor = Colors.es_Background,
                                    periodSelectorSelectedContainerColor = Colors.es_PrimaryCard,
                                    periodSelectorUnselectedContainerColor = Colors.es_Background
                                ))
                            Button(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Colors.ss_AccentColor
                                ),
                                onClick = { expandedTime = false }) {
                                Text(text = "Назначить")
                            }
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
            val sqlDate = LocalDateTime.of(
                date.year,
                date.monthValue+1,
                date.dayOfMonth,
                time.hour,
                time.minute,
                0,
                0
                ).toInstant(ZoneOffset.UTC).toEpochMilli()
            Timber.e(sqlDate.toString())
            viewModel.createUserMeeting(
                ApiMeeting(
                    meetingTitle = eventName,
                    meetingBody = description,
                    meetingGame = selectedGame,
                    meetingGenre = selectedGenre,
                    meetingStatus = true,
                    meetingGeoMarker = selectedPlace.values.first(),
                    meetingCountPlayers = playerCount.toInt(),
                    meetingDate = sqlDate.toString(),
                )
            )
            goToEvents()
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Colors.ss_AccentColor
            )
            ) {
            Text("Создать")
        }
    }
}

@Composable
fun ShowDatePicker(date: () -> LocalDate) {

}

@Composable
fun ShowTimeePicker() {
    AndroidView(
        { CalendarView(it) },
        modifier = Modifier.wrapContentWidth(),
        update = { views ->
            views.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            }
        }
    )
}


@Preview
@Composable
fun CreateEventsPreview(){
    CreateEventScreen(hiltViewModel(), {})
}
