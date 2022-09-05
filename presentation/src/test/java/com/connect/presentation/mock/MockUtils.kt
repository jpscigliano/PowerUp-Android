package com.connect.presentation.mock

import com.connect.domain.model.*
import com.connect.presentation.powerUpList.PowerUpsScreenUiState

fun mockListOfPowerUps(addInvalidPowerUp: Boolean): List<PowerUp> {
    return mutableListOf<PowerUp>().apply {
        if (addInvalidPowerUp) {
            add(PowerUp(title = Title(value = "Invalid PowerUp"),
                description = Description(value = "Im an invalid powerUp. Click me for error"),
                longDescription = Description(value = "Im just an invalid power up. Im here only to force an error while fetching data from the repository."),
                isConnected = true,
                storeImage = ImageUrl(value = "https://www.seekpng.com/png/detail/334-3345964_error-icon-download-attention-symbol.png"),
                storeUrl = StoreUrl(value = "")
            ))
        }

        add(PowerUp(
            title = Title(value = "Power 1"),
            description = Description(value = "This is the description for Power 1"),
            longDescription = Description(value = "This is the long description for Power 1"),
            isConnected = true,
            storeImage = ImageUrl(value = "no image"),
            storeUrl = StoreUrl(value = "store url"))
        )
        add(PowerUp(
            title = Title(value = "Power 2"),
            description = Description(value = "This is the description for Power 2"),
            longDescription = Description(value = "This is the long description for Power 2"),
            isConnected = true,
            storeImage = ImageUrl(value = "no image"),
            storeUrl = StoreUrl(value = "store url"))
        )
        add(PowerUp(
            title = Title(value = "Power 3"),
            description = Description(value = "This is the description for Power 3"),
            longDescription = Description(value = "This is the long description for Power 3"),
            isConnected = true,
            storeImage = ImageUrl(value = "no image"),
            storeUrl = StoreUrl(value = "store url"))
        )
        add(PowerUp(
            title = Title(value = "Power 4"),
            description = Description(value = "This is the description for Power 4"),
            longDescription = Description(value = "This is the long description for Power 4"),
            isConnected = true,
            storeImage = ImageUrl(value = "no image"),
            storeUrl = StoreUrl(value = "store url")))
    }


}

fun mockPowerUpsScreenUiState(powerUpList: List<PowerUp>): PowerUpsScreenUiState {
    return PowerUpsScreenUiState(
        isLoading = false,
        activePowerUps = powerUpList.filter { it.isConnected },
        availablePowerUps = powerUpList.filter { !it.isConnected },
    )
}
