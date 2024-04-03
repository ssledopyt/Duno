package com.example.duno.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.duno.data.Meeting
import com.example.duno.db.ApiHelper
import com.example.duno.db.ApiMeeting
import com.example.duno.db.DataStatus
import com.example.duno.ui.LargeDp
import com.example.duno.ui.SmallDp
import com.example.duno.ui.TinyDp
import com.example.duno.viewmodel.MainViewModel
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            //       .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Events Screen" }
    ) {
        var text by remember { mutableStateOf("") } // Query for SearchBar
        var active by remember { mutableStateOf(false) } // Active state for SearchBar
        SearchBar(modifier = Modifier
            .fillMaxWidth(0.95f)
            .align(Alignment.CenterHorizontally)
            .height(50.dp)
            .offset(y = 6.dp),
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
                Text(text = "Enter your query",
                    fontSize = 14.sp)
            },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }) {}

        Box(modifier = Modifier.padding(top = TinyDp, start = SmallDp)){
            PreviewFilterChipGroup()
        }
        Box(modifier = Modifier
            .padding(top = TinyDp)
            .padding(horizontal = TinyDp)){
            //lateinit var viewModel: MainViewModel = HiltViewModelFactory()

            
            val messages = mutableListOf(
                Meeting("twtw"),
                Meeting("twtw"),
                Meeting("twtw"),
                Meeting("twtw"),
                Meeting("twtw"),
                Meeting("twtw"),
                Meeting("twtw"),
                Meeting("tw32tw"),
                Meeting("twtw"),
            )
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            //val meetingViewModel = MeetingViewModel()
            val mainViewModel: MainViewModel = hiltViewModel()
            var meetingUIState = mainViewModel.meetingList.observeAsState()
            Card(modifier = Modifier.align(Alignment.Center).size(140.dp)){
                Timber.e("Ya korobka?")
                when (meetingUIState.value!!.status){
                    DataStatus.Status.LOADING ->{
                        Timber.e("Grujus")
                    }
                    DataStatus.Status.ERROR ->{
                        Timber.e("Oshibka")
                        Timber.e(meetingUIState.value!!.message)
                    }
                    DataStatus.Status.SUCCESS ->{
                        val title = meetingUIState.value!!.data!!.meetingTitle
                        Timber.e(title)
                        Text(text = title)
                    }
                }

            }
            //EventsList(messages, listState, meetingViewModel)
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp),
                onClick = {
                    coroutineScope.launch{ listState.animateScrollToItem(index = 0)  }
                }
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowUp,contentDescription = null)
            }
        }
    }
}

@Composable
fun EventsList(
    eventsList: List<Meeting>,
    listState: LazyListState,
    //viewModel: MeetingViewModel
){
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        ){
        items(eventsList) { event ->
            EventsDetails(event = event)
        }
    }

}

@Composable
fun EventsDetails(
    modifier: Modifier = Modifier,
    event: Meeting
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(150.dp))
            /*FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                image.tags.forEach { tag ->
                    SuggestionChip(
                        label = {
                            Text(text = tag)
                        },
                        onClick = {},
                    )
                }
            }*/
            //Spacer(Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipGroup(
    items: List<String>,
    defaultSelectedItemIndex:Int = 0,
    selectedItemIcon: ImageVector = Icons.Filled.Done,
    itemIcon: ImageVector = Icons.Filled.Build,
    onSelectedChanged : (Int) -> Unit = {}
){
    var selectedItemIndex by remember { mutableStateOf(defaultSelectedItemIndex) }

    LazyRow(userScrollEnabled = true,
        modifier = Modifier.height(LargeDp),

        ) {

        items(items.size) { index: Int ->
            FilterChip(
                modifier = Modifier.padding(top = TinyDp, end = TinyDp),
                selected = items[selectedItemIndex] == items[index],
                onClick = {
                    selectedItemIndex = index
                    onSelectedChanged(index)
                },
                label = { Text(items[index]) },
                leadingIcon = if (items[selectedItemIndex] == items[index]) {
                    {
                        Icon(
                            imageVector = selectedItemIcon,
                            contentDescription = "Localized Description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    {
                        Icon(
                            imageVector = itemIcon,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PreviewFilterChipGroup() {
    FilterChipGroup(items = listOf("Ближайшие мероприятия", "Без опыта", "Есть опыт", "Очень опытные"),
        onSelectedChanged = {

        })
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