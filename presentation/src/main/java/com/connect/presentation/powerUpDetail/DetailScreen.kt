package com.connect.presentation.powerUpDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.connect.presentation.BaseEvent
import com.connect.presentation.R
import com.connect.presentation.common.*
import com.connect.presentation.extensions.observeWithLifecycle
import com.connect.presentation.extensions.rememberStateWithLifecycle
import com.connect.presentation.theme.TibberTheme


@Composable
fun DetailScreen(
    toolbarTitle: String,
    navigateUp: () -> Unit,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val viewModel: PowerUpDetailViewModel = hiltViewModel()
    val viewState: PowerUpDetailScreenUiState by rememberStateWithLifecycle(viewModel.screenUiState)

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

    DetailScreen(
        toolbarTitle = toolbarTitle,
        viewState = viewState,
        scaffoldState = scaffoldState,
        navigateUp = navigateUp)


}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    toolbarTitle: String,
    viewState: PowerUpDetailScreenUiState,
    scaffoldState: ScaffoldState,
    navigateUp: () -> Unit,
) {


    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = toolbarTitle,
                showBackArrow = true,
                onNavigateUp = navigateUp
            )
        },
        content = {

            TibberLoading(viewState.isLoading)

            PowerUpDetailScreenContent(
                modifier = Modifier.padding(it),
                uiState = viewState,
                showContent = viewState.isLoading.not() && viewState.showErrorView.not())

            ErrorView(
                modifier = Modifier.padding(it),
                showContent = viewState.showErrorView
            )

        },
        scaffoldState = scaffoldState,
        snackbarHost = { DefaultSnackbar(snackbarHostState = it) }
    )

}


@Composable
internal fun PowerUpDetailScreenContent(
    modifier: Modifier,
    showContent: Boolean,
    uiState: PowerUpDetailScreenUiState,
) {

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {

            PowerUpHeaderDetail(uiState.detailHeaderUiState)

            PowerUpButtons(
                modifier = Modifier.padding(top = 32.dp),
                isConnected = uiState.showConnectToPowerUpButton
            )

            MoreAboutPowerUp(
                modifier = Modifier.padding(top = 54.dp),
                moreAboutUiState = uiState.moreAboutUiState
            )
        }
    }

}

@Composable
internal fun PowerUpHeaderDetail(detailHeaderUiState: DetailHeaderUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = detailHeaderUiState.image,
            requestBuilder = { crossfade(true) },
            contentDescription = detailHeaderUiState.image,
            modifier = Modifier
                .clip(RoundedCornerShape(5))
                .size(96.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column() {
            Text(
                text = detailHeaderUiState.headerTitle,
                style = MaterialTheme.typography.h2
            )
            Text(
                text = detailHeaderUiState.headerDescription,
                style = MaterialTheme.typography.body1
            )

        }
    }
}

@Composable
internal fun PowerUpButtons(modifier: Modifier = Modifier, isConnected: Boolean) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 25.dp, start = 25.dp)
    ) {
        if (isConnected)
            DisconnectButton()
        else
            ConnectButton()

        BuyButton(modifier = Modifier.padding(top = 32.dp))
    }

}

@Composable
internal fun MoreAboutPowerUp(modifier: Modifier = Modifier, moreAboutUiState: MoreAboutUiState) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxHeight()

    ) {
        Text(
            text = stringResource(id = R.string.more_about, moreAboutUiState.title),
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = moreAboutUiState.description,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

}


@Preview
@Composable
internal fun MyPowerUpHeaderDetail() {
    TibberTheme {
        PowerUpHeaderDetail(
            DetailHeaderUiState(
                image = "",
                headerTitle = "Tibber Pulse",
                headerDescription = "See your energy consumption in real time"
            )
        )
    }
}

@Preview
@Composable
internal fun MyMoreAboutPowerUp() {
    TibberTheme {
        MoreAboutPowerUp(
            moreAboutUiState = MoreAboutUiState(
                title = "Tado°",
                description = "Donec velit lectus, euismod et ullamcorper eget, egestas eget dolor. Cras tristique quam sit amet diam posuere, id vehicula mi vulputate. Donec vitae mi quis ex consectetur rutrum. In gravida fermentum consectetur. Integer tempor mi nulla, at aliquet est ultrices quis. Cras congue eget orci non luctus. Sed quis turpis a risus pulvinar mollis.\n" +
                        "\n" +
                        "Quisque risus sem, facilisis ac risus sit amet, pharetra tempor dolor. Vivamus in enim nunc. Sed auctor sit amet nisi a euismod. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nullam nisi tellus, blandit pulvinar lacinia id, cursus id leo. Aliquam ut blandit erat. Praesent lacinia maximus massa eget elementum. Vestibulum ornare nisl a quam lacinia dictum eget maximus turpis. Aenean non sollicitudin velit.\n" +
                        "\n" +
                        "Vivamus bibendum tellus faucibus pulvinar ultricies. Mauris consectetur massa vitae elementum fermentum. Proin ullamcorper nisi faucibus turpis efficitur scelerisque. Integer eu purus vel mauris vestibulum accumsan eget nec mauris. Cras sit amet consectetur mauris. Nunc eu elementum metus. Phasellus eleifend ex eu purus tincidunt viverra. Quisque nunc enim, ornare vitae congue sed, porttitor eget eros. Nunc pretium leo sit amet mollis ultricies."
            )
        )
    }
}

@Preview
@Composable
fun MyDetailScreen() {
    TibberTheme {
        DetailScreen(toolbarTitle = "Tesla") {}
    }
}

@Preview
@Composable
fun MyToolbarWithNavigation() {
    TibberTheme {
        Toolbar("Toolbar with navigation", true, {})
    }

}

@Preview
@Composable
fun MyToolbarWithoutNavigation() {
    TibberTheme {
        Toolbar("Toolbar without navigation", false, {})
    }

}

