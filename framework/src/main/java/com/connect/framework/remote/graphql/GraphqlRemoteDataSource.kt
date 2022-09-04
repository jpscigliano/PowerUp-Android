package com.connect.framework.remote.graphql

import com.apollographql.apollo3.ApolloClient
import com.connect.data.datasource.RemoteDatasource
import com.connect.domain.DataSourceResult
import com.connect.domain.Mapper
import com.connect.domain.model.*
import com.connect.framework.GetPowerUpsQuery
import com.connect.framework.GetPowerUpsWithOnbardingQuery
import com.connect.framework.remote.utils.ErrorParser
import com.connect.framework.remote.utils.getApolloRemoteResult
import com.connect.framework.remote.utils.toList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GraphqlRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    private val assignmentDatumToPowerUpMapper: Mapper<GetPowerUpsQuery.AssignmentDatum, PowerUp>,
    private val errorParser: ErrorParser,
) : RemoteDatasource {

    override suspend fun getPowerUps(): DataSourceResult<List<PowerUp>> {
        getPowerUpsWithOnboarding()

        return getApolloRemoteResult(
            mapModelToRequestDto = {},
            call = { apolloClient.query(GetPowerUpsQuery()) },
            mapResponseDtoToModel = { queryData ->
                queryData.assignmentData?.let { assignmentDatumToPowerUpMapper.toList().map(it) }
                    ?: listOf()
            },
            errorParser = errorParser
        )
    }

    suspend fun getPowerUpsWithOnboarding() {
        getApolloRemoteResult(
            mapModelToRequestDto = {},
            call = { apolloClient.query(GetPowerUpsWithOnbardingQuery()) },
            mapResponseDtoToModel = { queryData ->
                queryData.preOnboarding
                queryData.assignmentData
            },
            errorParser = errorParser
        )
    }

    override suspend fun getPowerUp(id: ID): DataSourceResult<PowerUp> {
        return DataSourceResult.Success(
            PowerUp(
                title = Title(value = "2"),
                description = Description(value = "3"),
                longDescription = Description(value = "4"),
                isConnected = false,
                storeImage = ImageUrl(value = ""),
                storeUrl = StoreUrl(value = "")
            )
        )
    }
}