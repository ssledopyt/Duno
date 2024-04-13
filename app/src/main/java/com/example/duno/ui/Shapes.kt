/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duno.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object DunoSizes {
    val standartCommonButtonHeight = 48.dp
    val standartCommonButtonWidht = 48.dp
    val standartFABHeight = 48.dp
    val standartFABWidht = 48.dp

    val superTinyDp = 4.dp
    val tinyDp = 8.dp
    val smallDp = 12.dp
    val standartDp = 24.dp
    val mediumDp = 36.dp
    val largeDp = 48.dp
}


val Shapes = Shapes(
    small = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 0.dp
    ),
    medium = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 0.dp
    )
)
