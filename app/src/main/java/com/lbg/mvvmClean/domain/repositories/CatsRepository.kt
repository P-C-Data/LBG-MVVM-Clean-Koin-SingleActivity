package com.lbg.mvvmClean.domain.repositories


import com.lbg.mvvmClean.data.NetworkResult
import com.lbg.mvvmClean.data.models.catData.CatResponse
import com.lbg.mvvmClean.data.models.catData.FavouriteCatsItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CatsRepository {
    suspend fun fetchCats(limit: Int = 10): Response<List<CatResponse>>
    suspend fun fetchFavouriteCats(subId: String): Response<List<FavouriteCatsItem>>
    suspend fun insertFavouriteCats(data: List<FavouriteCatsItem>): List<Long>?
    suspend fun fetchTestFavouriteCats(subId: String): Flow<NetworkResult<List<FavouriteCatsItem>>>

}