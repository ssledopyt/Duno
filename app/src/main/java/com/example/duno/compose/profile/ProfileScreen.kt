package com.example.duno.compose.profile

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes
import com.example.duno.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    goToSignUp: ()->Unit,
    goToUserEvents: (String) -> Unit,
    goToAddEvent: () -> Unit,
    goToEvents: () -> Unit,
    userName: String,
    userNickname: String
) {
/*    var enabled by remember { mutableStateOf(true) }

    LaunchedEffect(enabled) {
        if (enabled) return@LaunchedEffect
        else delay(1000L)  
        enabled = true
    }*/
    BackHandler {
        goToEvents()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.vistaWhite),
    ) {
        // Отображение имени и фамилии
        TopBarProfile()
        panelProfileNickname(userName, userNickname)
        Spacer(Modifier.height(ProfileDefaultPadding))
        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .height(170.dp)
            .padding(16.dp)
        ){
            CardAddingEvent(goToAddEvent)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            ClickableTextProfile(text = "Мои мероприятия", onClick = { goToUserEvents("Мои мероприятия") })
            ClickableTextProfile(text = "Избранное", onClick = { goToUserEvents("Избранное")})
            ClickableTextProfile(text = "Архив событий", onClick = { goToUserEvents("Архив событий")})
        }
        Spacer(Modifier.height(ProfileDefaultPadding))
        //AboutApp()
        Spacer(Modifier.height(ProfileDefaultPadding))
        Box (modifier = Modifier.fillMaxWidth()){
            ExitUser(userViewModel,goToSignUp)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardAddingEvent(
    goToAddEvent: () -> Unit,
    ){
    Card(
        modifier = Modifier
            .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            goToAddEvent()
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
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Твое мероприятие",
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(modifier = Modifier,
                            onClick = {
                                goToAddEvent()
                            }) {
                                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null, tint = Color.Red)
                        }
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Найди новых друзей и новых знакомых!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 2
            )
        }
    }
}


@Composable
fun ClickableTextProfile(text: String, onClick: () -> Unit){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = DunoSizes.smallDp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Rounded.DateRange,
                contentDescription = null
            )
            ClickableText(
                modifier = Modifier
                    .padding(start = 12.dp, end = DunoSizes.mediumDp),
                text = AnnotatedString(text),
                onClick = { onClick() })
        }
        Icon(imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
    }
}


@Composable
fun AboutApp() {
    OutlinedButton(onClick = {
    },
        border = BorderStroke(1.dp, Color.DarkGray),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(40.dp)
        ) {
        Text(text = "Помощь")
    }
}

@Composable
fun ExitUser(
    userViewModel: UserViewModel,
    goToSignUp: () -> Unit,
) {
    var corot = rememberCoroutineScope()
    val context  = LocalContext.current
    var alert = remember { mutableStateOf(false) }
    var exit = remember {mutableStateOf(false)}

    AnimatedVisibility(visible = alert.value) {
        AlertDialog(onDismissRequest = { alert.value=false },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Colors.ss_BackGround),
                    onClick = { alert.value = false }) {
                    Text(text = "Нет", color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall)
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Colors.ss_BackGround),
                    onClick = { exit.value = true}) {
                    Text(text = "Да", color = Colors.es_Cancel,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall)
                }
        },
            containerColor = Colors.ss_BackGround,
            title = {
                Text(text = "Вы точно хотите выйти?",
                style = MaterialTheme.typography.headlineSmall)})
    }
    if (exit.value) {
        LaunchedEffect(true) {
            Toast.makeText(
                context,
                "Выходим...",
                Toast.LENGTH_LONG
            ).show()
            corot.launch {
                delay(3000)
                userViewModel.logOutUser()
                goToSignUp()
            }
        }
    }
    Button(onClick = {
        alert.value=true

    },
        shape = RoundedCornerShape(DunoSizes.standartDp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = DunoSizes.smallDp, end = DunoSizes.smallDp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Colors.md_Background)
    ) {
        Text(
            text = "ВЫЙТИ",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall)
    }
}

@Composable
fun TopBarProfile(

) {
    Box(
        modifier = Modifier
            .height(76.dp)
            .fillMaxWidth()
    ){
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 20.dp),
                text = "Профиль",
                style = MaterialTheme.typography.headlineSmall,
                )
        }
    }

}

@Composable
fun panelProfileNickname(
    userName: String,
    userNickname: String,
){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .background(Colors.ss_AccentColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = Color.White
            )
            Column (Modifier.padding(horizontal = 8.dp)){
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.White

                    //modifier = modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = userNickname,
                    modifier = Modifier
                        .padding(),
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        }
    }
}

/*@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = event.description,
                modifier = Modifier.padding(top = DunoSizes.tinyDp)
            )
        }
    }
}

data class User(
    val name: String,
    val surname: String,
    val nickname: String
)

data class Event(
    val title: String,
    val description: String,
)

val user = User(
    name = "Иван",
    surname = "Иванов",
    nickname = "@yullfwrg"
)

val events = listOf(
    Event(
        title = "Встреча с друзьями",
        description = "Встреча с друзьями в кафе",
    ),
    Event(
        title = "Поход в кино",
        description = "Поход в кино на новый фильм",
    ),
)*/

@Preview
@Composable
fun ProfileScreenPreview() {
    //ProfileScreen()
}

private val ProfileDefaultPadding = 12.dp