package com.example.duno.compose.profile

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes
import com.example.duno.viewmodel.UserViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    navController: NavController,
    goToSignUp: ()->Unit,
    goToUserEvents: (String) -> Unit,
    userName: String,
    userNickname: String
) {
/*    var enabled by remember { mutableStateOf(true) }

    LaunchedEffect(enabled) {
        if (enabled) return@LaunchedEffect
        else delay(1000L)
        enabled = true
    }*/
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.es_Background),
    ) {
        // Отображение имени и фамилии
        TopBarProfile(userName,userNickname)
        Spacer(Modifier.height(ProfileDefaultPadding))
        Column(modifier = Modifier.fillMaxWidth()) {
            ClickableTextProfile(text = "Мои мероприятия", icon = Icons, onClick = { goToUserEvents("Мои мероприятия") })
            ClickableTextProfile(text = "Избранное", icon = Icons, onClick = { goToUserEvents("Избранное") })
            ClickableTextProfile(text = "Архив событий", icon = Icons, onClick = { goToUserEvents("Архив событий") })
        }
        Spacer(Modifier.height(ProfileDefaultPadding))
        AboutApp()
        Spacer(Modifier.height(ProfileDefaultPadding))
        Box (modifier = Modifier.fillMaxSize()){
            ExitUser(userViewModel,goToSignUp)
        }
    }
}


@Composable
fun ClickableTextProfile(text: String,icon: Icons, onClick: () -> Unit){
    Row {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DunoSizes.standartDp, vertical = DunoSizes.smallDp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            //Icon(imageVector = Icons.Rounded.DateRange, contentDescription = null)
            ClickableText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = DunoSizes.mediumDp),
                text = AnnotatedString(text),
                onClick = {onClick()})
            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null)
        }
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
            .padding(start = DunoSizes.smallDp, end = DunoSizes.smallDp)
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
    Button(onClick = {
        userViewModel.logOutUser()
        goToSignUp()
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
    userName: String,
    userNickname: String,
) {
    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .background(Colors.md_PrimaryContainer)
    ){
        Column {
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .padding(top = DunoSizes.standartDp)
                //modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = userNickname,
                modifier = Modifier
                    .padding()
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.labelLarge,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
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
)

@Preview
@Composable
fun ProfileScreenPreview() {
    //ProfileScreen()
}

private val ProfileDefaultPadding = 12.dp