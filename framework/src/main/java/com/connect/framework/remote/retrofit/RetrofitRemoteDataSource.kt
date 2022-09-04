package com.connect.framework.remote.retrofit

import com.connect.data.datasource.RemoteDatasource
import com.connect.domain.DataSourceResult
import com.connect.domain.model.*


/**
 * In case we need to use Retrofit or Volley or anmi kind off http client a DataSource could be create.
 */
class RetrofitRemoteDataSource() : RemoteDatasource {
  override suspend fun getPowerUps(): DataSourceResult<List<PowerUp>> {
    return DataSourceResult.Success(listOf())
  }

  override suspend fun getPowerUp(id: ID): DataSourceResult<PowerUp> {
    return DataSourceResult.Success(
      PowerUp(
        title = Title(value = "From Retrofit"),
        description = Description(value = "Just  some mock data to show  how to use DataSources and discuss during interview"),
        longDescription = Description(value = ""),
        isConnected = false,
        storeImage = ImageUrl(value = ""),
        storeUrl = StoreUrl(value = "")
      )
    )
  }
}