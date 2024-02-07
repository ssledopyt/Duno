package com.example.duno

class MainActivity {
    @Composable
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
    }
}