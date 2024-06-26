package com.example.duno.compose.elements

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipGroup(
    items: List<String>,
    defaultSelectedItemIndex:Int = 0,
    selectedItemIcon: ImageVector = Icons.Filled.Done,
    itemIcon: ImageVector = Icons.Filled.ArrowDropDown,
    onSelectedChanged : (Int) -> Unit = {}
){
    var selectedItemIndex by remember { mutableStateOf(defaultSelectedItemIndex) }

    LazyRow(userScrollEnabled = true,
        modifier = Modifier.height(DunoSizes.largeDp),

        ) {

        items(items.size) { index: Int ->
            FilterChip(
                enabled = items[selectedItemIndex] == items[index],
                modifier = Modifier.padding(top = DunoSizes.tinyDp, end = DunoSizes.tinyDp),
                selected = items[selectedItemIndex] == items[index],
                onClick = {
                    selectedItemIndex = index
                    onSelectedChanged(index)
                },
                label = { Text(items[index]) },
                leadingIcon = if (items[selectedItemIndex] == items[index]) {
                    {
                        Icon(
                            imageVector = selectedItemIcon,
                            contentDescription = "Localized Description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    {
                        Icon(
                            imageVector = itemIcon,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Colors.es_Background,
                    disabledContainerColor = Colors.md_Primary,
                    selectedContainerColor = Colors.es_PrimaryCard),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = false,
                    selected = false,
                    borderWidth = 2.dp,
                    borderColor = Colors.es_PrimaryCard),
            )
        }
    }
}

@Preview
@Composable
fun PreviewFilterChipGroup() {
    FilterChipGroup(items = listOf("Ближайшие мероприятия", "Без опыта", "Есть опыт", "Очень опытные"),
        onSelectedChanged = {

        })
}