package com.connect.data.util


import com.connect.domain.DataSourceResult
import com.connect.domain.DataSourceResult.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * [selectQuery] - Executes a select to be sent as cache data
 * [asyncCall] - Makes an async call (e.g. network call), returns the result wrapped in a [DataSourceResultHolder]
 * [insertResultQuery] - Saves the network response as new cache data
 *
 * Function notify UI about:
 * [DataSourceResultHolder.Status.SUCCESS] - with data from database
 * [DataSourceResultHolder.Status.ERROR] - if error has occurred with Local data if availables
 * [DataSourceResultHolder.Status.IN_PROGRESS]
 */
fun <MODEL> fetchAndObserveLocalDatasource(
  selectQuery: suspend () -> Flow<DataSourceResult<MODEL>>,
  asyncCall: (suspend () -> DataSourceResult<MODEL>)? = null,
  insertResultQuery: (suspend (MODEL) -> Unit)? = null
): Flow<DataSourceResult<MODEL>> {
  return flow {
    // notify the process started when there is an async call
    // otherwise we just select from database and therefore no need for loading status
    if (asyncCall != null) emit(InProgress())

    // get cache
    var selectResponse = selectQuery()

    // get remote result, will also hold success status
    asyncCall?.let {
      // call network
      val responseStatus = asyncCall()

      when (responseStatus) {
        is Success -> {
          // save data to room if successful
          insertResultQuery?.invoke(responseStatus.data!!)
          // submit new local and listen for any future changes
          selectResponse = selectQuery()
          emitAll(selectResponse)
        }
        is Error -> {
          //Emit API error with local data
          emit(responseStatus.mapWithData(selectResponse.firstOrNull()?.dataOrNull()))
        }
        else -> {
          emit(responseStatus)
        }

      }
    } ?: run { // if there is no network call, submit local and listen for any future changes
      emitAll(selectResponse)
    }
  }
}

/**
 *
 * Function notify UI about:
 * [DataSourceResultHolder.Status.SUCCESS] - success with response
 * [DataSourceResultHolder.Status.ERROR] - if error has occurred
 * [DataSourceResultHolder.Status.IN_PROGRESS]
 *
 * @param asyncCall Makes an async call (e.g. network call), returns the result wrapped in a [DataSourceResultHolder]
 * @param insertResultQuery Saves the network response as new cache data
 */
fun <MODEL> fetchRemoteAndStore(
  asyncCall: (suspend () -> DataSourceResult<MODEL>),
  insertResultQuery: (suspend (MODEL) -> Unit)? = null
): Flow<DataSourceResult<MODEL>> {
  return flow {

    // notify the process started
    emit(InProgress())

    // call network
    val responseStatus = asyncCall()

    // save to db
    if (responseStatus is Success) {
      insertResultQuery?.invoke(responseStatus.data!!)
    }

    // emit response
    emit(responseStatus)
  }
}

/**
 *
 * Function returns one status, without notifying about progress
 *
 * @param asyncCall Makes an async call (e.g. network call), returns the result wrapped in a [DataSourceResultHolder]
 */
suspend fun <MODEL> fetchRemoteOnce(
  asyncCall: (suspend () -> DataSourceResult<MODEL>),
  insertResultQuery: (suspend (MODEL) -> Unit)? = null
): DataSourceResult<MODEL> {
  return withContext(Dispatchers.IO) {
    // call network
    val responseStatus = asyncCall()

    // save data if successful
    if (responseStatus is Success) {
      insertResultQuery?.invoke(responseStatus.data!!)
    }

    responseStatus
  }
}

/**
 * Save data, that's it
 */
@JvmName("insertResultOnce")
suspend fun insertToRemoteOnce(
  insertResultQuery: suspend () -> Unit
): DataSourceResult<Boolean> {
  return withContext(Dispatchers.IO) {
    insertResultQuery()
    Success(true)
  }
}

/**
 * [selectQuery] - Executes a select to be sent as cache data
 */
suspend fun <MODEL> fetchRemoteOnce(
  selectQuery: suspend () -> DataSourceResult<MODEL>
): DataSourceResult<MODEL> {
  return withContext(Dispatchers.IO) {
    selectQuery()
  }
}