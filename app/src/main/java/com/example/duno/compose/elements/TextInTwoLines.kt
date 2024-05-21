package com.example.duno.compose.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.duno.ui.DunoSizes

@Composable
fun TextInTwoLines(
    leftText: String,
    rightText:String,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = DunoSizes.tinyDp),
    ) {
        Text(modifier = Modifier.padding(bottom = 4.dp),
            text = leftText,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = rightText,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}