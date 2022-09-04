package com.connect.framework.remote.utils

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.connect.domain.AppError
import com.connect.domain.DataSourceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext




    /**
     * Get APOLLO response
     *
     * @param mapModelToRequestDto prepares the model to be sent in the request - if nothing is supposed to be sent then initialize with '{}'
     * @param call api.
     * @param mapResponseDtoToModel transforms from the api response model to the domain module.
     */
    suspend fun <REQUEST_API_MODEL, MODEL, QUERY_DATA : Operation.Data> getApolloRemoteResult(
        mapModelToRequestDto: (suspend () -> REQUEST_API_MODEL)? = null,
        call: suspend (REQUEST_API_MODEL?) -> ApolloCall<QUERY_DATA>,
        mapResponseDtoToModel: (suspend (QUERY_DATA) -> MODEL),
        errorParser: ErrorParser,
    ): DataSourceResult<MODEL> {
        return try {
            // verify internet
            if (withContext(Dispatchers.IO) { !NetworkingUtils.hasInternetConnection() }) {
                DataSourceResult.NoInternet()
            } else {
                // call api
                val response:ApolloResponse<QUERY_DATA> = call(mapModelToRequestDto?.invoke()).execute()
                //Map errors
                response.errors?.let {
                    DataSourceResult.Error(AppError.TibberError.CustomerTibberError)
                } ?: run {
                    //Map response
                    mapResponseDtoToModel(response.data as QUERY_DATA).let { model ->
                        DataSourceResult.Success(model)
                    }
                }
            }
        } catch (exception: Exception) {
            // Parse Exceptions
            DataSourceResult.Error(errorParser.parseException(exception))
        }
    }

