package com.clean.mvvmLgb.data.services.cats

import com.clean.mvvmLgb.data.models.catData.CatResponse
import com.clean.mvvmLgb.data.models.catData.FavouriteCatsItem
import retrofit2.Response

interface CatApiServiceHelper {
    suspend fun fetchCatsImages(limit: Int): Response<List<CatResponse>>
    suspend fun fetchFavouriteCats(subId: String): Response<List<FavouriteCatsItem>>

}