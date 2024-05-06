package com.example.duno.compose.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.duno.viewmodel.MeetingViewModel
import com.example.duno.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoadingScreen(
    meetingViewModel: MeetingViewModel,
    userViewModel: UserViewModel,
    userNickname: String,
    navigateToEvents: () -> Unit
)  {
    val coroutineScope = rememberCoroutineScope()
    val meetingUIState by meetingViewModel.meetingList.observeAsState()
    LaunchedEffect(true){
        coroutineScope.launch {
            delay(1000)
            meetingViewModel.getUserLikes(userNickname)
            delay(1000)
            meetingViewModel.getAllMeetings()
            delay(1000)
            userViewModel.getAllPlaces()
            delay(1000)
            navigateToEvents()
            Timber.e("Add all to lists")
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        CircularProgressIndicator(
            modifier= Modifier.align(Alignment.Center).size(40.dp)
        )
    }

}
