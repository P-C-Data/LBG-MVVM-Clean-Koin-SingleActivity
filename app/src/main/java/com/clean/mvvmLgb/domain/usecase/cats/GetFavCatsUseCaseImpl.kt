package com.clean.mvvmLgb.domain.usecase.cats

import com.clean.mvvmLgb.data.NetworkResult
import com.clean.mvvmLgb.domain.mappers.CatDataModel
import com.clean.mvvmLgb.domain.mappers.mapFavCatsDataItems
import com.clean.mvvmLgb.domain.repositories.CatsRepository
import com.clean.mvvmLgb.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetFavCatsUseCaseImpl(private val catRepo: CatsRepository) : GetFavCatsUseCase {
    override suspend fun execute() = flow<NetworkResult<List<CatDataModel>>> {
        emit(NetworkResult.Loading())
        with(catRepo.fetchFavouriteCats(Constants.SUB_ID)) {
            if (isSuccessful) {
                val catDataList = this.body()?.map { cat ->
                    cat.mapFavCatsDataItems()
                }
                emit(NetworkResult.Success(catDataList?.reversed()))
                this.body()?.let { catRepo.insertFavouriteCats(it) }
            } else {
                emit(NetworkResult.Error(this.errorBody().toString()))
            }
        }
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }
}
