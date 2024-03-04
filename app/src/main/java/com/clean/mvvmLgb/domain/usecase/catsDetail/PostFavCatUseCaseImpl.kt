package com.clean.mvvmLgb.domain.usecase.catsDetail

import com.clean.mvvmLgb.data.NetworkResult
import com.clean.mvvmLgb.data.models.catDetails.PostFavCatModel
import com.clean.mvvmLgb.domain.mappers.CallSuccessModel
import com.clean.mvvmLgb.domain.mappers.mapSuccessData
import com.clean.mvvmLgb.domain.repositories.CatDetailsRepository
import com.clean.mvvmLgb.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PostFavCatUseCaseImpl(private val catDetailsRepo: CatDetailsRepository) : PostFavCatUseCase {
    override suspend fun execute(imageId: String) = flow<NetworkResult<CallSuccessModel>> {
        emit(NetworkResult.Loading())
        val favCat = PostFavCatModel(imageId, Constants.SUB_ID)
        with(catDetailsRepo.postFavouriteCat(favCat)) {
            if (isSuccessful) {
                emit(NetworkResult.Success(this.body()?.mapSuccessData()))
                this.body()?.id?.let { catDetailsRepo.insertFavouriteCat(it, favCat.imageId) }
            } else {
                emit(NetworkResult.Error(this.errorBody().toString()))
            }
        }
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }
}
