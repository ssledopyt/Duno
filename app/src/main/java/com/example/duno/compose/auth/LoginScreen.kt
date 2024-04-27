package com.example.duno.compose.auth


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.duno.R
import com.example.duno.compose.elements.HeaderText
import com.example.duno.compose.elements.LoginTextField
import com.example.duno.viewmodel.UserViewModel
import timber.log.Timber


@Composable
fun LoginScreen(
    isLoggedIn: Boolean,
    navController: NavController,
    viewModel: UserViewModel,
    goToMainScreen: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    if (isLoggedIn) {
        goToMainScreen()
    }
    val snackbarHostState = remember { SnackbarHostState() }
/*    if (viewModel.isUserLoggedIn) {
        Timber.e("Im bot ${viewModel.isUserLoggedIn}")
            goToMainScreen()
    }*/

    val (userNickname, setNickname) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val (checked, onCheckedChange) = rememberSaveable {
        mutableStateOf(false)
    }
    val isFieldsEmpty = userNickname.isNotEmpty() && password.isNotEmpty()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Войти",
            modifier = Modifier
                .padding(vertical = defaultPadding)
                .align(alignment = Alignment.Start)
        )
        LoginTextField(
            value = userNickname,
            onValueChange = setNickname,
            labelText = "Имя пользователя",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = password,
            onValueChange = setPassword,
            labelText = "Пароль",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(itemSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checked, onCheckedChange = onCheckedChange)
                Text("Запомнить меня")
            }
            TextButton(onClick = {}) {
                Text("Забыли пароль?")
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Button(
            onClick = {
                viewModel.loginToServer(userNickname,password)
                if (viewModel.isUserLoggedIn){
                    Timber.e("Go to mainScreen with ${viewModel.isUserLoggedIn}")
                    goToMainScreen()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsEmpty
        ) {
            Text("Войти")
        }
        AlternativeLoginOptions(
            onIconClick = { index ->
                when (index) {
                    0 -> {
                        Toast.makeText(context, "Яндекс", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onSignUpClick = onSignUpClick,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        )

    }
}


@Composable
fun AlternativeLoginOptions(
    onIconClick: (index: Int) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconList = listOf(
        R.drawable.icon_yandex,
/*        R.drawable.icon_vk,
        R.drawable.icon_github,
        R.drawable.icon_google,*/
    )
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Или войдите с помощью", modifier = Modifier.padding(bottom = defaultPadding))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            iconList.forEachIndexed { index, iconResId ->
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = "alternative Login",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onIconClick(index)
                        }
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(defaultPadding))
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ещё нет аккаунта?")
            Spacer(Modifier.height(itemSpacing))
            TextButton(onClick = onSignUpClick) {
                Text("Зарегистрироваться")
            }
        }
    }

}

private val defaultPadding = 16.dp
private val itemSpacing = 8.dp


@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    //LoginScreen(isLoggedIn = false, {}, {},{})
}