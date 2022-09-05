package com.connect.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connect.presentation.R
import com.connect.presentation.theme.TibberTheme
import com.connect.presentation.theme.button
import com.connect.presentation.theme.tibberGrey900
import com.connect.presentation.theme.tibberRed


@Composable
fun ConnectButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    TibberButton(
        modifier = modifier,
        text = stringResource(id = R.string.connect_tibber),
        textColor = Color.White,
        onClick = onClick,
        buttonColors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
    )
}

@Composable
fun DisconnectButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    TibberButton(
        modifier = modifier.border(width = 1.dp, color = tibberRed, shape = Shapes().button),
        text = stringResource(id = R.string.disconnect_tibber),
        textColor = tibberRed,
        onClick = onClick,
        buttonColors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)
    )


}

@Composable
fun BuyButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    TibberButton(
        modifier = modifier,
        text = stringResource(id = R.string.buy_at_tibber),
        onClick = onClick,
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        )

    )
}


@Composable
fun TibberButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    buttonColors: ButtonColors,
    text: String = "",
    textColor: Color = tibberGrey900,
) {
    Button(
        onClick = onClick,
        content = {
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = textColor
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = Shapes().button,
        colors = buttonColors,
    )
}


@Preview
@Composable
private fun MyConnectButton() {
    TibberTheme {
        ConnectButton()
    }

}

@Preview
@Composable
private fun MyDisconnectButton() {
    TibberTheme {
        DisconnectButton()
    }
}

@Preview
@Composable
private fun MyBuyButton() {
    TibberTheme {
        BuyButton()
    }
}
