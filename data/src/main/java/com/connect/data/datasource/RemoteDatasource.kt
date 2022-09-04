package com.connect.data.datasource

import com.connect.domain.DataSourceResult
import com.connect.domain.model.ID
import com.connect.domain.model.PowerUp

interface RemoteDatasource {
  suspend fun getPowerUps(): DataSourceResult<List<PowerUp>>
  suspend fun getPowerUp(id: ID): DataSourceResult<PowerUp>
}