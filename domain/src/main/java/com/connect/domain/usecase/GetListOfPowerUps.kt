package com.connect.domain.usecase

import com.connect.domain.DataSourceResult
import com.connect.domain.model.*
import com.connect.domain.repository.PowerUpsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListOfPowerUps @Inject constructor(
    private val powerUpsRepository: PowerUpsRepository,
) : BaseUseCase<Unit, Flow< @JvmSuppressWildcards DataSourceResult<List<@JvmSuppressWildcards PowerUp>>>> {
    override suspend operator fun invoke(params: Unit): Flow<DataSourceResult<List<PowerUp>>> {

        return powerUpsRepository.getPowerUpsList().map {

            if (it is DataSourceResult.Success) {
                val newListWithAnInvalidPowerUp: List<PowerUp>? =
                    it.data?.toMutableList()?.apply {
                        add(0,
                            PowerUp(title = Title(value = "Invalid PowerUp"),
                                description = Description(value = "Im an invalid powerUp. Click me for error"),
                                longDescription = Description(value = "Im just an invalid power up. Im here only to force an error while fetching data from the repository."),
                                isConnected = true,
                                storeImage = ImageUrl(value = "https://www.seekpng.com/png/detail/334-3345964_error-icon-download-attention-symbol.png"),
                                storeUrl = StoreUrl(value = "")
                            )
                        )
                    }
                it.mapWithData(newListWithAnInvalidPowerUp)
            } else {
                it
            }

        }
    }

}