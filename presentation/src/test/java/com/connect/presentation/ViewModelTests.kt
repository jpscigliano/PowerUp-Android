@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package com.connect.presentation

import app.cash.turbine.test
import com.connect.domain.DataSourceResult
import com.connect.domain.model.PowerUp
import com.connect.domain.repository.PowerUpsRepository
import com.connect.domain.usecase.BaseUseCase
import com.connect.domain.usecase.GetListOfPowerUps
import com.connect.presentation.mock.mockListOfPowerUps
import com.connect.presentation.mock.mockPowerUpsScreenUiState
import com.connect.presentation.powerUpList.PowerUpsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ViewModelTests {


    private val repository: PowerUpsRepository = mockk()

    private val getListOfPowerUps: BaseUseCase<Unit, Flow<DataSourceResult<List<PowerUp>>>> =
        GetListOfPowerUps(powerUpsRepository = repository)

    private lateinit var powerUpsViewModel: PowerUpsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        coEvery { repository.getPowerUpsList() } returns
                flowOf(DataSourceResult.InProgress(),
                    DataSourceResult.Success(mockListOfPowerUps(false)))

        powerUpsViewModel = PowerUpsViewModel(getListOfPowerUps)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN GetListOfPowerUps is invoke THEN  PowerUpsScreenUiState is emitted`() = runTest {
        powerUpsViewModel.powerUpState.test {
            assertEquals(awaitItem().isLoading, true)
            assertEquals(awaitItem(), mockPowerUpsScreenUiState(mockListOfPowerUps(true)))
        }

    }
}