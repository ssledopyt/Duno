package com.example.duno

import android.os.Bundle
import androidx.activity.ComponentActivity
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    /*@Composable
    fun HelloScreen() {
        var name by rememberSaveable { mutableStateOf("") }

        HelloContent(name = name, onNameChange = { name = it })
    }

    @Composable
    fun HelloContent(name: String, onNameChange: (String) -> Unit) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hello, $name",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedTextField(value = name, onValueChange = onNameChange, label = { Text("Name") })
        }
    }*/
}