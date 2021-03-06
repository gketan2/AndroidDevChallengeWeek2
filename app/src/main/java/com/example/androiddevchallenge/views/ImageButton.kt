package com.example.androiddevchallenge.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CircularImageButton(imageVector: ImageVector, size: Dp = 50.dp, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .padding(0.dp),
        shape = CircleShape,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "play/pause"
        )
    }
}

@Composable
fun ImageButton(
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    enabled: Boolean = true,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
            tint = MaterialTheme.colors.onPrimary
        )
    }
}