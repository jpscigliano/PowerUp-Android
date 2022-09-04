package com.connect.domain.model

import com.connect.domain.utils.getPositive

data class PowerUp(
    val title: Title,
    val description: Description,
    val longDescription: Description,
    val isConnected: Boolean,
    val storeImage: ImageUrl,
    val storeUrl: StoreUrl,
) {
    val id: ID =
        ID((title.value + description.value  + storeUrl.value).hashCode()
            .getPositive()
            .toString())


}
