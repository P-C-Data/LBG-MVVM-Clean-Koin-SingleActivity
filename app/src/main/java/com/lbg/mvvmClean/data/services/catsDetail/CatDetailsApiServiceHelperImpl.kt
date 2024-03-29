package com.lbg.mvvmClean.data.services.catsDetail

import com.lbg.mvvmClean.data.models.SuccessResponse
import com.lbg.mvvmClean.data.models.catDetails.PostFavCatModel
import com.lbg.mvvmClean.data.services.CatsService
import retrofit2.Response

class CatDetailsApiServiceHelperImpl(private val service: CatsService) :
    CatDetailsApiServiceHelper {
    override suspend fun postFavouriteCat(favCat: PostFavCatModel): Response<SuccessResponse> =
        service.postFavouriteCat(favCat)

    override suspend fun deleteFavouriteCat(favouriteId: Int): Response<SuccessResponse> =
        service.deleteFavouriteCat(favouriteId)
}