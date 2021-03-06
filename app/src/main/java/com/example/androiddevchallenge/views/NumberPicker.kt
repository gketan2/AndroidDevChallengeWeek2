package com.example.androiddevchallenge.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Picker(
    range: IntRange,
    indicator: String,
    textStyle: TextStyle = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
    valueState: MutableState<Long>
) {
    Column(
        modifier = Modifier.width(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StepButton(range = range, valueState = valueState)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = String.format("%02d$indicator", valueState.value),
            style = textStyle.copy(color = MaterialTheme.colors.onPrimary),
        )
        Spacer(modifier = Modifier.height(8.dp))
        StepButton(range = range, multiplier = -1, valueState = valueState)
    }
}