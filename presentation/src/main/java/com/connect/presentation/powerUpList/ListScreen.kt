package com.connect.presentation.powerUpList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.connect.domain.model.*
import com.connect.presentation.BaseEvent
import com.connect.presentation.R
import com.connect.presentation.common.AsyncImage
import com.connect.presentation.common.DefaultSnackbar
import com.connect.presentation.common.TibberLoading
import com.connect.presentation.common.Toolbar
import com.connect.presentation.extensions.observeWithLifecycle
import com.connect.presentation.extensions.rememberStateWithLifecycle
import com.connect.presentation.theme.TibberTheme
import kotlin.random.Random


@Composable
fun ListScreen(onPowerUpSelected: (powerUp: PowerUp) -> Unit) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val viewModel: PowerUpsViewModel = hiltViewModel()
    val viewState by rememberStateWithLifecycle(viewModel.powerUpState)

    val context = LocalContext.current
    viewModel.message.observeWithLifecycle {
        val message = when (it) {
            is BaseEvent.ShowError, is BaseEvent.ShowGenericError -> context.getString(R.string.generic_error)
            is BaseEvent.ShowNoInternetMessage -> context.getString(R.string.no_internet_error)
            is BaseEvent.ShowPowerUpInvalidMessage -> context.getString(R.string.power_up_not_found_error)
        }
        scaffoldState.snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short)
    }

    ListScreen(
        scaffoldState = scaffoldState,
        powerUpsScreenUiModel = viewState,
        onPowerUpSelected = onPowerUpSelected
    )

}


@Composable
internal fun ListScreen(
    scaffoldState: ScaffoldState,
    powerUpsScreenUiModel: PowerUpsScreenUiState,
    onPowerUpSelected: (powerUp: PowerUp) -> Unit,
) {
    Scaffold(
        topBar = {
            Toolbar(
                title = "Power-Ups",
                showBackArrow = false,
            )
        },
        content = {

            TibberLoading(showLoading = powerUpsScreenUiModel.isLoading)
            ListScreenContent(
                modifier = Modifier.padding(it),
                showContent = powerUpsScreenUiModel.isLoading.not(),
                activePowerUps = powerUpsScreenUiModel.activePowerUps,
                availablePowerUps = powerUpsScreenUiModel.availablePowerUps,
                onPowerUpSelected = onPowerUpSelected)
        },
        scaffoldState = scaffoldState,
        snackbarHost = { DefaultSnackbar(snackbarHostState = it) }
    )
}

@Composable
private fun ListScreenContent(
    modifier: Modifier = Modifier,
    showContent: Boolean,
    activePowerUps: List<PowerUp>,
    availablePowerUps: List<PowerUp>,
    onPowerUpSelected: (powerUp: PowerUp) -> Unit = {},
) {

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        val lazyListState = rememberLazyListState()
        LazyColumn(
            state = lazyListState,
            modifier = modifier.padding(horizontal = 15.dp),
            content = {
                PowerUpsListUi(
                    groupHeaderStringRes = R.string.header_active_power_up,
                    powerUps = activePowerUps,
                    onPowerUpSelected = onPowerUpSelected
                )
                PowerUpsListUi(
                    groupHeaderStringRes = R.string.header_active_power_up,
                    powerUps = availablePowerUps,
                    onPowerUpSelected = onPowerUpSelected
                )
            },
        )
    }
}


private fun LazyListScope.PowerUpsListUi(
    groupHeaderStringRes: Int,
    powerUps: List<PowerUp>,
    onPowerUpSelected: (powerUp: PowerUp) -> Unit,
) {
    if (powerUps.isNotEmpty()) {
        item { HeaderItem(stringResource(id = groupHeaderStringRes)) }
        items(
            items = powerUps,
            key = { it.id.value },
            itemContent = { powerUp: PowerUp ->
                PowerUpRow(
                    powerUp = powerUp,
                    onPowerUpSelected = onPowerUpSelected
                )
            },
        )
    }
}


@Composable
internal fun HeaderItem(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.h2,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
internal fun PowerUpRow(powerUp: PowerUp, onPowerUpSelected: (powerUp: PowerUp) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onPowerUpSelected(powerUp) },
        elevation = 0.dp,

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val powerUpImageUrl = powerUp.storeImage.value
            val name: String = powerUp.title.value
            val description: String = powerUp.description.value

            AsyncImage(
                model = powerUpImageUrl,
                requestBuilder = { crossfade(true) },
                contentDescription = name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(5)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1
                )

            }
        }
    }
}


@Preview
@Composable
private fun MyHeaderItem() {
    TibberTheme {
        HeaderItem(label = "Active power-ups")
    }
}


@Preview
@Composable
private fun MyPowerUpRow() {
    TibberTheme { PowerUpRow(powerUp = mockPowerUp(), {}) }
}

@Preview
@Composable
private fun MyPowerUpList() {
    TibberTheme {
        ListScreenContent(
            showContent = true,
            activePowerUps = listOf(mockPowerUp(), mockPowerUp()),
            availablePowerUps = listOf(mockPowerUp(), mockPowerUp()),
        )
    }
}


private fun mockPowerUp(): PowerUp {
    return PowerUp(
        longDescription = Description(value = "Ngenic ${Random.nextInt()}"),
        isConnected = true,
        storeImage = ImageUrl(value = "https://tibber-app-gateway.imgix.net/images/powerup/tile/ngenic.png"),
        title = Title(value = "Ngenic"),
        description = Description(value = "Ngenic is a smart thermostat to control your water based heating"),
        storeUrl = StoreUrl(value = "https://tibber.com/se/store/produkt/pulse")
    )
}

