package com.connect.framework.di

import com.connect.data.datasource.RemoteDatasource
import com.connect.domain.Mapper
import com.connect.domain.model.PowerUp
import com.connect.framework.GetPowerUpsQuery
import com.connect.framework.remote.graphql.GraphqlRemoteDataSource
import com.connect.framework.remote.graphql.mapper.AssignmentDataToPowerUpList
import com.connect.framework.remote.utils.AppErrorParser
import com.connect.framework.remote.utils.ErrorParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FrameworkModule {

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(remoteDataSource: GraphqlRemoteDataSource): RemoteDatasource


    @Binds
    abstract fun provideAssignmentDatumMapper(mapper: AssignmentDataToPowerUpList): Mapper<GetPowerUpsQuery.AssignmentDatum, PowerUp>

    @Binds
    abstract fun provideErrorParser(errorParser: AppErrorParser): ErrorParser
}