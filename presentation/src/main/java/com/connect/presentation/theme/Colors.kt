package com.connect.presentation.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


internal val tibberblue200 = Color(0xFF23B8CC)
internal val tibberGrey = Color(0xFFE5E5E5)
internal val tibberGrey400 = Color(0xFF7F7F7F)
internal val tibberGrey600 = Color(0xFF4A4A4A)
internal val tibberGrey900 = Color(0xFF3A4654)
internal val tibberRed = Color(0xFFC9162B)


val TibberLightColors = lightColors(
    primary = tibberblue200,
    secondary = Color.White,
    background = tibberGrey,
    surface = Color.White,
    error = tibberRed
)

val TibberDarkColors = darkColors(
    primary = tibberblue200,
    secondary = Color.Black,
    background = tibberGrey,
    surface = Color.Black,
    error = tibberRed
)