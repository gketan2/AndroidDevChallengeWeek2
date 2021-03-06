package com.example.androiddevchallenge.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun StepButton(
    range: IntRange,
    multiplier: Int = 1,
    valueState: MutableState<Long>
) {
    IconButton(
        onClick = {
            val newValue = valueState.value + 1 * multiplier
            if (newValue in range) {
                valueState.value = newValue
            }
        }
    ) {
        Icon(
            imageVector = if (multiplier > 0) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = "",
            tint = MaterialTheme.colors.onPrimary
        )
    }
}