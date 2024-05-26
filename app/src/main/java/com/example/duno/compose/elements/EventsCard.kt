package com.example.duno.compose.elements

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.duno.db.ApiMeeting
import com.example.duno.ui.Colors
import com.example.duno.viewmodel.MeetingViewModel
import timber.log.Timber
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsCard(
    meetingViewModel: MeetingViewModel,
    userNickname: String,
    event: ApiMeeting,
    userLike: Boolean,
    goToEventDetails: (Int, Boolean) -> Unit,
    userLikes: List<Int>,
) {
    var userLikeEvent = remember { mutableStateOf(userLike) }
    Timber.tag("UserLikesSET").e(userLikes.toString())
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            event.meetingId?.let { goToEventDetails(it, userLikes.contains(it)) }
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
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = event.meetingTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconLike(userLikeEvent,meetingViewModel,userNickname,event)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(Modifier) {
                            IconStatus(status = event.meetingStatus)
                            Text(modifier = Modifier.padding(start = 8.dp),
                                text = event.clubName,
                                //style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Light,
                            )
                        }
                        Text(
                            text = "${get_correct_dateTime(startsAtDate.dayOfMonth)} ${get_month(startsAtDate.monthValue)} " +
                                    "в ${get_correct_dateTime(startsAtDate.hour)}:${get_correct_dateTime(startsAtDate.minute)}",
                            //style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Box{
                Text(
                    text = event.meetingBody.ifEmpty { "Пока пусто" },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                /* хотел сделать градиент заливку в конце текста будто блюр
                Row (
                    modifier = Modifier.align(Alignment.BottomEnd),
                    verticalAlignment = Alignment.Bottom
                ){
                    Spacer(
                        modifier = Modifier
                            .width(48.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color.Transparent, Color.White)
                                )
                            )
                    )
                }*/
            }
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