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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.duno.ui.DunoSizes
import com.example.duno.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
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
    val selectedPlace = remember { mutableStateMapOf<String, List<Float>>("" to listOf<Float>(0.0F, 0.0F))}
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
            text = "Мероприятие",
            modifier = Modifier
                .padding(vertical = DunoSizes.smallDp)
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
        //GAME
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
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
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedGame,
                onDismissRequest = {
                    expandedGame = false
                }
            ) {
                Timber.e("trying to load list1")
                if (games[0]==null || games.isEmpty()) {
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
                .padding(horizontal = DunoSizes.smallDp),
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
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedGenre,
                onDismissRequest = {
                    expandedGenre = false
                }
            ) {
                Timber.e("trying to load list2")
                if (genres[0]==null || genres.isEmpty()) {
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
                .padding(horizontal = DunoSizes.smallDp),
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
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedPlace,
                onDismissRequest = {
                    expandedPlace = false
                }
            ) {
                Timber.e("trying to load list3")
                if (location[0]==null || location.isEmpty()) {
                    Text("Загрузка...")
                }else {
                    location.forEach { locate ->
                        DropdownMenuItem(
                            text = {
                                Text("${locate!!.nameClub}: ${locate.place}")
                            },
                            onClick = {
                                selectedPlace.clear()
                                selectedPlace[locate!!.nameClub] = locate.geoMarker
                                Timber.e(selectedPlace.keys.toString())
                                Timber.e(selectedPlace.toString())
                                expandedPlace = false
                            }
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
            onValueChange = {
                when {
                    Regex("^[1-9]\$").matches(it) -> playerCount = it
                    Regex("^1[0-6]\$").matches(it) -> playerCount = it
                    Regex("").matches(it) -> playerCount = it
                }
            },
            label = { Text("Количество игроков (от 1 до 16)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = TextFieldValue(text = "${date.dayOfMonth}.${date.monthValue+1}.${date.year}"),
                onValueChange = {},
                modifier = Modifier,
                readOnly = true)
            Button(onClick = { expandedDate=true }) {
                
            }
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.smallDp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = TextFieldValue(text ="${time.hour}:${time.minute}"),
                onValueChange = {},
                modifier = Modifier,
                readOnly = true)
            Button(onClick = { expandedTime=true }) {

            }
        }
        if (expandedDate){
            DatePickerDialog(
                modifier = Modifier.wrapContentSize(),
                confirmButton = {
                    Button(onClick = { expandedDate = false }) {
                    Text(text = "Назначить")}
                },
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
                                date = LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(dayOfMonth)
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
                        .padding(16.dp)) {
                        Column(modifier = Modifier
                            .wrapContentSize()) {
                            TimePicker(state = time,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(16.dp))
                            Button(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 16.dp),
                                onClick = { expandedTime = false }) {
                                Text(text = "Назначить")
                            }
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.padding(DunoSizes.smallDp))

        Button(onClick = {
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
        }) {
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

@Composable
fun LayerTime(){
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    /*val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )*/
}

@Preview
@Composable
fun CreateEventsPreview(){
    CreateEventScreen(hiltViewModel(), {})
}
