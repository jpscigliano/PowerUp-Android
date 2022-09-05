package com.connect.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(
    title: String,
    showBackArrow: Boolean,
    onNavigateUp: () -> Unit = {},
) {

    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.h1) },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = if (showBackArrow) {
            {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        "Back",
                        tint = Color.White
                    )
                }
            }
        } else {
            null
        },
        actions = {},
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = 12.dp,
    )
}
