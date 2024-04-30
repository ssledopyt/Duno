package com.example.duno.compose.auth


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.duno.compose.elements.HeaderText
import com.example.duno.compose.elements.LoginTextField
import com.example.duno.db.ApiUser
import com.example.duno.viewmodel.UserViewModel
import java.time.LocalDate

@Composable
fun RegistrationScreen(
    isLoggedIn: Boolean,
    navController: NavController,
    viewModel: UserViewModel,
    goToMainScreen: () -> Unit,
    onSignUpClick: (ApiUser) -> Unit,
    onLoginClick: () -> Unit,
    onPolicyClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    if (isLoggedIn) {
        goToMainScreen()
    }

    val (firstName, onFirstNameChange) = rememberSaveable { mutableStateOf("") }
    val (lastName, onLastNameChange) = rememberSaveable { mutableStateOf("") }
    val (nickname, onNicknameChange) = rememberSaveable { mutableStateOf("") }
    val (email, onEmailChange) = rememberSaveable { mutableStateOf("") }
    val (password, onPasswordChange) = rememberSaveable { mutableStateOf("") }
    val (confirmPassword, onConfirmPasswordChange) = rememberSaveable { mutableStateOf("") }
    val (agree, onAgreeChange) = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var isPasswordSame by remember { mutableStateOf(false) }
    val isFieldsNotEmpty = firstName.isNotEmpty() && lastName.isNotEmpty() &&
            email.isNotEmpty() && password.isNotEmpty() &&
            confirmPassword.isNotEmpty()
            //&& agree


    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(isPasswordSame) {
            Text(
                "Пароли не совпадают!",
                color = MaterialTheme.colorScheme.error,
            )
        }
        HeaderText(
            text = "Регистрация",
            modifier = Modifier.padding(vertical = defaultPadding)
                .align(alignment = Alignment.Start)
        )
        LoginTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            labelText = "Имя",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(defaultPadding))
        LoginTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            labelText = "Фамилия",
            modifier = Modifier.fillMaxWidth()
        )
        LoginTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            labelText = "Никнейм",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(defaultPadding))
        LoginTextField(
            value = email,
            onValueChange = onEmailChange,
            labelText = "Почта",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(defaultPadding))
        LoginTextField(
            value = password,
            onValueChange = onPasswordChange,
            labelText = "Пароль",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password
        )
        Spacer(Modifier.height(defaultPadding))
        LoginTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            labelText = "Подтвердите пароль",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password
        )
        Spacer(Modifier.height(defaultPadding))
/*        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val privacyText = "Политикой конфиденциальности"
            val policyText = "Условиями использования"
            val annotatedString = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("Я согласен с")
                }
                append(" ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    pushStringAnnotation(tag = privacyText, privacyText)
                    append(privacyText)
                }
                append(" И ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    pushStringAnnotation(tag = policyText, policyText)
                    append(policyText)
                }
            }

            Checkbox(agree, onAgreeChange)
            ClickableText(
                annotatedString,
            ) { offset ->
                annotatedString.getStringAnnotations(offset, offset).forEach {
                    when (it.tag) {
                        privacyText -> {
                            Toast.makeText(context, "Политика конфиденциальности Text Clicked", Toast.LENGTH_SHORT)
                                .show()
                            onPrivacyClick()
                        }

                        policyText -> {
                            Toast.makeText(context, "Условия использования Text Clicked", Toast.LENGTH_SHORT)
                                .show()
                            onPolicyClick()
                        }
                    }
                }
            }
        }*/
        Spacer(Modifier.height(defaultPadding + 8.dp))
        Button(
            onClick = {
                isPasswordSame = password != confirmPassword
                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
                //val phoneRegex = "(\\+\\d( )?)?([-\\( ]\\d{3}[-\\) ])( )?\\d{3}-\\d{4}"
                if (!isPasswordSame) {
                    if (email.matches(emailRegex.toRegex())){
                        val user = ApiUser(
                            userName = firstName,
                            userSecondName = lastName,
                            userNickname = nickname,
                            userEmail = email,
                            userPassword = password,
                            userCreatedAt = LocalDate.now().toString())
                        onSignUpClick(user)
                        if (viewModel.userState.value!!.isError){
                            Toast.makeText(context, viewModel.userState.value!!.error, Toast.LENGTH_SHORT).show()
                        }else{
                            goToMainScreen()
                        }
                    }else{
                        Toast.makeText(context, "Такой почты не существует!", Toast.LENGTH_SHORT).show()
                        //TODO parol
                    }
                }else{
                    Toast.makeText(context, "Пароли не совпадают!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsNotEmpty,
        ) {
            Text("Регистрация")
        }
        Spacer(Modifier.height(defaultPadding))
        val singTx = "Войти"
        val signInAnnotation = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                append("Уже есть аккаунт?")
            }
            append("  ")
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                pushStringAnnotation(singTx, singTx)
                append(singTx)
            }


        }
        ClickableText(
            signInAnnotation,
        ) { offset ->
            signInAnnotation.getStringAnnotations(offset, offset).forEach {
                if (it.tag == singTx) {
                    Toast.makeText(context, "Sign in Clicked", Toast.LENGTH_SHORT).show()
                    onLoginClick()
                }
            }
        }

    }
}

private val defaultPadding = 16.dp
private val itemSpacing = 8.dp

@Preview(showSystemUi = true)
@Composable
fun RegistrationScreenPreview(){
    //RegistrationScreen({}, {}, {}, {})
}
    /*    LaunchedEffect(key1 = startNewActivity.value){
            if(startNewActivity.value){
                activity.startActivity(Intent(activity, OYndxAuth::class.java))
            }
        }*/


