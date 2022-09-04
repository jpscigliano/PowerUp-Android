package com.connect.domain.repository

import com.connect.domain.DataSourceResult
import com.connect.domain.model.ID
import com.connect.domain.model.PowerUp
import kotlinx.coroutines.flow.Flow

interface PowerUpsRepository {
  suspend fun getPowerUpsList(): Flow<DataSourceResult<List<PowerUp>>>
  suspend fun getPowerUp(id: ID): Flow<DataSourceResult<PowerUp>>
}