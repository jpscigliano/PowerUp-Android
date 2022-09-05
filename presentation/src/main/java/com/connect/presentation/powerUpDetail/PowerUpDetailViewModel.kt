package com.connect.presentation.powerUpDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connect.domain.AppError.NetworkError
import com.connect.domain.AppError.TibberError
import com.connect.domain.DataSourceResult
import com.connect.domain.model.ID
import com.connect.domain.usecase.GetPowerUpDetail
import com.connect.presentation.BaseEvent

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PowerUpDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPowerUpDetail: GetPowerUpDetail,
) : ViewModel() {


    private val powerUpId: ID = ID(savedStateHandle.get<String>("powerUpId")!!)

    private val _screenUiState: MutableStateFlow<PowerUpDetailScreenUiState> =
        MutableStateFlow(PowerUpDetailScreenUiState(isLoading = true))
    val screenUiState: StateFlow<PowerUpDetailScreenUiState> = _screenUiState

    private val _Messages: MutableSharedFlow<BaseEvent> = MutableSharedFlow()
    val message: SharedFlow<BaseEvent> = _Messages

    init {
        viewModelScope.launch {
            delay(2000)
            getPowerUpDetail.invoke(powerUpId).collect { useCaseResult ->
                when (useCaseResult) {
                    is DataSourceResult.InProgress -> _screenUiState.emit(
                        _screenUiState.value.copy(isLoading = true)
                    )
                    is DataSourceResult.Error -> {
                        _screenUiState.emit(
                            PowerUpDetailScreenUiState(isLoading = false, showErrorView = true)
                        )
                        val errorEvent = when (useCaseResult.error) {
                            is NetworkError -> BaseEvent.ShowGenericError
                            is TibberError.PowerUpNotFound -> BaseEvent.ShowPowerUpInvalidMessage
                            else -> BaseEvent.ShowGenericError
                        }
                        _Messages.emit(errorEvent)
                    }
                    is DataSourceResult.NoInternet -> {
                        _screenUiState.emit(
                            PowerUpDetailScreenUiState(isLoading = false, showErrorView = true)
                        )
                        _Messages.emit(BaseEvent.ShowNoInternetMessage)
                    }
                    is DataSourceResult.Success -> {

                        useCaseResult.data?.let { powerUp ->
                            _screenUiState.emit(
                                PowerUpDetailScreenUiState(
                                    isLoading = false,
                                    showErrorView = false,
                                    showConnectToPowerUpButton = powerUp.isConnected,
                                    detailHeaderUiState = DetailHeaderUiState(
                                        headerTitle = powerUp.title.value,
                                        headerDescription = powerUp.description.value,
                                        image = powerUp.storeImage.value),
                                    moreAboutUiState = MoreAboutUiState(title = powerUp.title.value,
                                        description = powerUp.longDescription.value),

                                    )
                            )
                        }

                    }
                }

            }

        }
    }

}