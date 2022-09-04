package com.connect.framework.remote.utils

import com.apollographql.apollo3.exception.ApolloHttpException
import com.connect.domain.AppError
import com.connect.domain.AppError.*
import javax.inject.Inject

interface ErrorParser {
    fun parseException(exception: Exception): AppError
    fun parseTibberError(code: String): TibberError
}

class AppErrorParser @Inject constructor() : ErrorParser {
    override fun parseException(exception: Exception): AppError {
        return when (exception) {
            is ApolloHttpException -> parseApolloError(exception.statusCode)
            else -> UnknownGenericError
        }
    }

    private fun parseApolloError(responseErrorCode: Int): NetworkError {
        return when (responseErrorCode) {
            400 -> NetworkError.InvalidRequest
            404 -> NetworkError.NotFound
            // etc..
            else -> NetworkError.UnknownError
        }
    }

    override fun parseTibberError(tibberErrorCode: String): TibberError {
        return when (tibberErrorCode) {
            "CAN_NOT_FETCH_BY_ID" -> TibberError.PowerUpNotFound
            else -> TibberError.CustomerTibberError
        }
    }
}
