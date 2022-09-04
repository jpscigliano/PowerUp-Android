package com.connect.framework.remote.graphql.mapper

import com.connect.domain.Mapper
import com.connect.domain.model.*
import com.connect.framework.GetPowerUpsQuery
import javax.inject.Inject

class AssignmentDataToPowerUpList @Inject constructor() :
    Mapper<GetPowerUpsQuery.AssignmentDatum, PowerUp> {
    override fun map(input: GetPowerUpsQuery.AssignmentDatum): PowerUp =
        PowerUp(
            title = Title(value = input.title),
            description = Description(value = input.description),
            longDescription = Description(value = input.longDescription),
            isConnected = input.connected,
            storeImage = ImageUrl(value = input.imageUrl),
            storeUrl = StoreUrl(value = input.storeUrl)
        )
}
