package com.connect.presentation

sealed class BaseEvent {
    data class ShowError(val error: String) : BaseEvent()
    object ShowGenericError : BaseEvent()
    object ShowNoInternetMessage: BaseEvent()
    object ShowPowerUpInvalidMessage : BaseEvent()
}