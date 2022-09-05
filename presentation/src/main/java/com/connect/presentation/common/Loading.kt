package com.connect.presentation.common


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connect.presentation.R
import com.connect.presentation.theme.TibberTheme




@Composable
fun TibberLoading(showLoading: Boolean, duration:Int=500) {

    AnimatedVisibility(
        visible = showLoading,
        enter = fadeIn(animationSpec = tween(durationMillis = duration)),
        exit = fadeOut(animationSpec = tween(durationMillis = duration))
    ) {
        TibberLoading(duration = duration)
    }

}

@Composable
fun TibberLoading(alpha: Float = 1f, duration:Int, jumpUpTo:Float=70f) {

    val infiniteTransition = rememberInfiniteTransition()

    val translation by infiniteTransition.animateFloat(
        initialValue = -0f,
        targetValue = -jumpUpTo,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = Ease),
            repeatMode = RepeatMode.Reverse
        )
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration * 2, easing = EaseInOutCirc),
            repeatMode = RepeatMode.Restart
        )
    )

    val size by infiniteTransition.animateFloat(
        initialValue = 1.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = EaseOutBack, delayMillis = 0),
            repeatMode = RepeatMode.Reverse
        )
    )

    LoadingIcon(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        translation = translation,
        rotation = rotation,
        size = size,
        alpha = alpha
    )

}

@Composable
private fun LoadingIcon(
    modifier: Modifier = Modifier,
    translation: Float,
    rotation: Float,
    size: Float,
    alpha: Float,
) {
    Box(
        modifier = modifier.alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .offset(y = translation.dp)
                    .rotate(rotation)
                    .scale(scaleX = size, scaleY = 1f / size)
            )
            Text(
                text = "LOADING..",
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .offset(y = translation.dp * 0.3f)
            )
        }
    }
}


@Preview
@Composable
fun MyTibberLoading() {
    TibberTheme() {
        TibberLoading(1f,500,70f)
    }

}