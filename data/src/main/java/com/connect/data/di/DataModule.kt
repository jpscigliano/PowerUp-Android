package com.connect.data.di


import com.connect.data.repository.PowerUpRepositoryImpl
import com.connect.domain.repository.PowerUpsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DotaModule will provide bindings for Repository abstractions.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

  @Binds
  @Singleton
  internal abstract fun bindPowerUpsRepository(charactersRepositoryImpl: PowerUpRepositoryImpl): PowerUpsRepository
}