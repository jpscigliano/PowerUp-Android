package com.connect.domain.usecase

interface BaseUseCase<PARAMS, USE_CASE_RESULT> {
    suspend operator fun invoke(params: PARAMS): USE_CASE_RESULT
}