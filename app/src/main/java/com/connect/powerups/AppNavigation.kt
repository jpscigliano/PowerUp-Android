/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalAnimationApi::class)

package com.connect.powerups

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.connect.presentation.powerUpDetail.DetailScreen
import com.connect.presentation.powerUpList.ListScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


internal sealed class Screen(val route: String) {
    object PowerUp : Screen("PowerUp")
}

private sealed class LeafScreen(val leafRoute: String) {
    fun createRoute(root: Screen) = "${root.route}/$leafRoute"

    object PowerUpList : LeafScreen(leafRoute = "powerUps")

    object PowerUpDetail : LeafScreen(leafRoute = "powerUps/{powerUpId}?{powerUpName}") {
        fun createRoute(root: Screen, powerUpId: String, powerUpName: String) =
            "${root.route}/powerUps/$powerUpId?$powerUpName"
    }

}


@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.PowerUp.route,
        enterTransition = { enterTransition(initialState, targetState) },
        exitTransition = { exitTransition(initialState, targetState) },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() },
        modifier = modifier,
    ) {
        addPowerUpTopLevel(navController)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addPowerUpTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.PowerUp.route,
        startDestination = LeafScreen.PowerUpList.createRoute(Screen.PowerUp),
    ) {
        addPowerUpList(navController, Screen.PowerUp)
        addPowerUpDetail(navController, Screen.PowerUp)

    }
}


private fun NavGraphBuilder.addPowerUpList(
    navController: NavController,
    root: Screen,
) {
    composable(route = LeafScreen.PowerUpList.createRoute(root)) {
        ListScreen(
            onPowerUpSelected = { powerUp ->
                navController.navigate(LeafScreen.PowerUpDetail.createRoute(root,
                    powerUp.id.value,
                    powerUp.title.value))
            }
        )
    }
}

private fun NavGraphBuilder.addPowerUpDetail(
    navController: NavController,
    root: Screen,
) {
    composable(route = LeafScreen.PowerUpDetail.createRoute(root)) { backStackEntry ->
        val name: String = backStackEntry.arguments?.getString("powerUpName") ?: ""
        DetailScreen(toolbarTitle = name, navigateUp = navController::navigateUp)
    }
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

private fun AnimatedContentScope<*>.enterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}


private fun AnimatedContentScope<*>.exitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private fun AnimatedContentScope<*>.popEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}


private fun AnimatedContentScope<*>.popExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}
