package com.connect.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connect.presentation.R
import com.connect.presentation.theme.TibberTheme


@Composable
internal fun ErrorView(modifier: Modifier, showContent: Boolean) {
    AnimatedVisibility(
        modifier = modifier,
        visible = showContent,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {

        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.error),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.upps_error),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h1
            )
            Icon(Icons.Rounded.Warning,
                modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .size(150.dp),
                contentDescription = "Error UI",
                tint = MaterialTheme.colors.background
            )
        }
    }
}

@Preview
@Composable
private fun MyErrorView() {
    TibberTheme {
        ErrorView(modifier = Modifier, showContent = true)
    }
}