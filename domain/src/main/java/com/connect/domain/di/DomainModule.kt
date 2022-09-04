package com.connect.domain.di

import com.connect.domain.DataSourceResult
import com.connect.domain.model.PowerUp

import com.connect.domain.usecase.BaseUseCase
import com.connect.domain.usecase.GetListOfPowerUps
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    @Singleton
     fun bindGetListOfPowerUps(getListOfPowerUps: GetListOfPowerUps)
            : BaseUseCase<Unit, Flow< @JvmSuppressWildcards DataSourceResult<List<@JvmSuppressWildcards PowerUp>>>>

}