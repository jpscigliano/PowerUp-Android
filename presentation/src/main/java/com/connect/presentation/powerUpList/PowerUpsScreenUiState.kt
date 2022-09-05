package com.connect.presentation.powerUpList

import com.connect.domain.model.PowerUp

data class PowerUpsScreenUiState(
    val isLoading: Boolean=false,
    val activePowerUps: List<PowerUp> = listOf(),
    val availablePowerUps: List<PowerUp> = listOf()
)