package com.connect.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.request.ImageRequest


@Composable
fun AsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    requestBuilder: (ImageRequest.Builder.() -> ImageRequest.Builder)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val modelX = requestBuilder?.let { builder ->
        when (model) {
            is ImageRequest -> model.newBuilder()
            else -> ImageRequest.Builder(LocalContext.current).data(model)
        }.apply { this.builder() }.build()
    } ?: model

    coil.compose.AsyncImage(
        model = modelX,
        modifier = modifier,
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale
    )
}
