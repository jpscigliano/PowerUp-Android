package com.connect.domain.usecase

import com.connect.domain.DataSourceResult
import com.connect.domain.model.ID
import com.connect.domain.model.PowerUp
import com.connect.domain.repository.PowerUpsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPowerUpDetail @Inject constructor(
    private val powerUpsRepository: PowerUpsRepository,
) : BaseUseCase<ID, Flow<DataSourceResult<PowerUp>>> {

    override suspend operator fun invoke(params: ID) =
        powerUpsRepository.getPowerUp(params)

}