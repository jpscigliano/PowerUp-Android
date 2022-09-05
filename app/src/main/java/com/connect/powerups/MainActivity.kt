package com.connect.powerups


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import com.connect.presentation.theme.TibberTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberAnimatedNavController()
            TibberTheme {
                Scaffold(
                    scaffoldState = rememberScaffoldState(),
                    snackbarHost= { SnackbarHost(it) },
                    content = {
                        AppNavigation(
                            navController = navController,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(it)
                        )
                    }
                )
            }
        }

    }
}