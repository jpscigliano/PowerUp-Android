package com.connect.presentation.powerUpList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connect.domain.AppError
import com.connect.domain.DataSourceResult
import com.connect.domain.model.PowerUp
import com.connect.domain.usecase.BaseUseCase
import com.connect.presentation.BaseEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PowerUpsViewModel @Inject constructor(
    private val getListOfPowerUps: BaseUseCase<Unit, Flow< @JvmSuppressWildcards DataSourceResult<List<@JvmSuppressWildcards PowerUp>>>>
) : ViewModel() {

    private val _PowerUpsScreenState: MutableStateFlow<PowerUpsScreenUiState> =
        MutableStateFlow(PowerUpsScreenUiState(isLoading = true))

    val powerUpState: StateFlow<PowerUpsScreenUiState> = _PowerUpsScreenState

    private val _Messages: MutableSharedFlow<BaseEvent> = MutableSharedFlow()
    val message: SharedFlow<BaseEvent> = _Messages

    init {
        viewModelScope.launch {
            getListOfPowerUps(Unit).collect { useCaseResult ->

                when (useCaseResult) {
                    is DataSourceResult.InProgress -> _PowerUpsScreenState.emit(
                        _PowerUpsScreenState.value.copy(isLoading = true)
                    )
                    is DataSourceResult.Success -> {

                        delay(2000)
                        _PowerUpsScreenState.emit(
                            _PowerUpsScreenState.value.copy(
                                isLoading = false,
                                activePowerUps = useCaseResult.data?.filter { it.isConnected }
                                    ?: listOf(),
                                availablePowerUps = useCaseResult.data?.filter { !it.isConnected }
                                    ?: listOf()
                            )
                        )


                    }
                    is DataSourceResult.NoInternet -> {
                        _PowerUpsScreenState.emit(
                            PowerUpsScreenUiState(isLoading = false)
                        )
                        _Messages.emit(BaseEvent.ShowNoInternetMessage)
                    }
                    is DataSourceResult.Error -> {
                        _PowerUpsScreenState.emit(
                            PowerUpsScreenUiState(isLoading = false)
                        )
                        val errorEvent = when (useCaseResult.error) {
                            is AppError.TibberError -> {
                                //Todo extra logic base on the Tibber Error
                                BaseEvent.ShowGenericError
                            }
                            else -> BaseEvent.ShowGenericError
                        }
                        _Messages.emit(errorEvent)
                    }
                }
            }
        }

    }


}