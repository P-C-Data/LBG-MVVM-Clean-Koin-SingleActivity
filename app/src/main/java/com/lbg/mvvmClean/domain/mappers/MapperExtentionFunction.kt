package com.lbg.mvvmClean.domain.mappers

import com.lbg.mvvmClean.data.models.SuccessResponse
import com.lbg.mvvmClean.data.models.catData.CatResponse
import com.lbg.mvvmClean.data.models.catData.FavouriteCatsItem

//CatData Mapper function used for Cat image listData at Cats
fun CatResponse.mapCatsDataItems(): CatDataModel {
    return CatDataModel(
        name = this.breeds[0].name,
        origin = this.breeds[0].origin,
        imageId = this.id,
        url = this.url
    )
}

fun FavouriteCatsItem.mapFavCatsDataItems(): CatDataModel {
    return CatDataModel(
        favId = this.id,
        url = this.image.url,
        imageId = this.imageId,
    )
}

fun SuccessResponse.mapSuccessData(): CallSuccessModel {
    return CallSuccessModel(
        successMessage = this.message,
        id = this.id
    )
}



