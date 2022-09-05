package com.connect.presentation.powerUpDetail

data class PowerUpDetailScreenUiState(
    val isLoading: Boolean = false,
    val showErrorView:Boolean=false,
    val showConnectToPowerUpButton: Boolean = false,
    val detailHeaderUiState: DetailHeaderUiState = DetailHeaderUiState(),
    val moreAboutUiState: MoreAboutUiState = MoreAboutUiState()
)

data class DetailHeaderUiState(
    val headerTitle: String = "",
    val headerDescription: String = "",
    val image: String = ""
)

data class MoreAboutUiState(
    val title: String = "",
    val description: String = "",
)

