package com.connect.data.repository

import com.connect.data.datasource.RemoteDatasource
import com.connect.domain.AppError
import com.connect.domain.DataSourceResult
import com.connect.domain.model.ID
import com.connect.domain.model.PowerUp
import com.connect.domain.repository.PowerUpsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PowerUpRepositoryImpl @Inject constructor(
    private val remoteDatasource: RemoteDatasource,
) : PowerUpsRepository {

    private val inMemoryCache: MutableList<PowerUp> = mutableListOf()

    override suspend fun getPowerUpsList(): Flow<DataSourceResult<List<PowerUp>>> = flow {
        emit(DataSourceResult.InProgress())
        val remoteDatasourceResult = remoteDatasource.getPowerUps()
        inMemoryCache.clear()
        inMemoryCache.addAll(remoteDatasourceResult.dataOrNull() ?: listOf())
        emit(remoteDatasourceResult)
    }

    override suspend fun getPowerUp(id: ID): Flow<DataSourceResult<PowerUp>> = flow {
        emit(DataSourceResult.InProgress())

        val powerUpResult = if (inMemoryCache.isEmpty()) {
            remoteDatasource.getPowerUps().dataOrNull()
        } else {
            inMemoryCache
        }?.find { it.id == id }

        if (powerUpResult != null)
            emit(DataSourceResult.Success(powerUpResult))
        else
            emit(DataSourceResult.Error(AppError.TibberError.PowerUpNotFound))
    }
}