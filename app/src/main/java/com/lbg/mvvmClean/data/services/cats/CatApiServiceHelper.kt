package com.lbg.mvvmClean.data.services.cats

import com.lbg.mvvmClean.data.models.catData.CatResponse
import com.lbg.mvvmClean.data.models.catData.FavouriteCatsItem
import retrofit2.Response

interface CatApiServiceHelper {
    suspend fun fetchCatsImages(limit: Int): Response<List<CatResponse>>
    suspend fun fetchFavouriteCats(subId: String): Response<List<FavouriteCatsItem>>

}