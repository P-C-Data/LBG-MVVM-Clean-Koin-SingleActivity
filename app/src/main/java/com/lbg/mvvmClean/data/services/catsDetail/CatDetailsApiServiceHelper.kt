package com.lbg.mvvmClean.data.services.catsDetail

import com.lbg.mvvmClean.data.models.SuccessResponse
import com.lbg.mvvmClean.data.models.catDetails.PostFavCatModel
import retrofit2.Response

interface CatDetailsApiServiceHelper {
    suspend fun postFavouriteCat(favCat: PostFavCatModel): Response<SuccessResponse>
    suspend fun deleteFavouriteCat(favouriteId: Int): Response<SuccessResponse>

}