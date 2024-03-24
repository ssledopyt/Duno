package com.example.duno.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.duno.data.Meeting

@Composable
fun ReplyDockedSearchBar(
    meetings: List<Meeting>,
    onSearchItemSelected: (Meeting) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchResults = remember { mutableStateListOf<Meeting>() }

/*    LaunchedEffect(query) {
        searchResults.clear()
        if (query.isNotEmpty()) {
            searchResults.addAll(
                meetings.filter {
                    it.title .startsWith(
                        prefix = query,
                        ignoreCase = true
                    ) || it.gameName.startsWith(
                        prefix = query,
                        ignoreCase = true
                    )
                    //дописать остальные условия
                }
            )
        }
    }*/
}