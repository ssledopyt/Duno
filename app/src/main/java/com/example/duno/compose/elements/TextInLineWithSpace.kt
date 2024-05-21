package com.example.duno.compose.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.duno.ui.DunoSizes

@Composable
fun TextInLineWithSpace(
    leftText: String,
    rightText:String,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = DunoSizes.tinyDp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically).padding(end = 8.dp),
            text = leftText,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = rightText,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}