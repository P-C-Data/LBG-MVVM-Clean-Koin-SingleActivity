package com.lbg.mvvmClean.data.repositories

import com.lbg.mvvmClean.data.NetworkResult
import com.lbg.mvvmClean.data.models.catData.FavouriteCatsItem
import com.lbg.mvvmClean.data.services.cats.CatApiServiceHelper
import com.lbg.mvvmClean.data.services.cats.CatsDatabaseHelper
import com.lbg.mvvmClean.domain.repositories.CatsRepository

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CatsRepositoryImpl(
    private val catsApiService: CatApiServiceHelper,
    private val catsDatabaseHelper: CatsDatabaseHelper
) : CatsRepository {
    override suspend fun fetchCats(limit: Int) = catsApiService.fetchCatsImages(limit)

    override suspend fun fetchFavouriteCats(subId: String) =
        catsApiService.fetchFavouriteCats(subId)

    override suspend fun insertFavouriteCats(data: List<FavouriteCatsItem>): List<Long> =
        catsDatabaseHelper.insertFavCatImageRelation(data)


    override suspend fun fetchTestFavouriteCats(subId: String) =
        flow<NetworkResult<List<FavouriteCatsItem>>> {
            emit(NetworkResult.Loading())
            with(catsApiService.fetchFavouriteCats(subId)) {
                if (isSuccessful) {
                    emit(NetworkResult.Success(this.body()))
                } else {
                    emit(NetworkResult.Error(this.errorBody().toString()))
                }
            }
        }.catch {
            emit(NetworkResult.Error(it.localizedMessage))
        }


}