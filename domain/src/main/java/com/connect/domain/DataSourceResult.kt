package com.connect.domain


sealed interface DataSourceResult<DATA> {
  class InProgress<DATA> : DataSourceResult<DATA>
  class NoInternet<DATA> : DataSourceResult<DATA>
  data class Error<DATA>(val error: AppError, val data: DATA? = null) :
    DataSourceResult<DATA>

  data class Success<DATA>(val data: DATA?) : DataSourceResult<DATA>

  fun dataOrNull(): DATA? {
    return when (this) {
      is Error -> this.data
      is Success -> this.data
      else -> null
    }
  }

  fun mapWithData(data: DATA?): DataSourceResult<DATA> {
    return when (this) {
      is Error -> this.copy(data = data)
      is Success -> this.copy(data = data)
      else -> this
    }
  }

  fun mapToError(error: Error<DATA>): DataSourceResult<DATA> {
    return error.mapWithData(data = dataOrNull())
  }
}


