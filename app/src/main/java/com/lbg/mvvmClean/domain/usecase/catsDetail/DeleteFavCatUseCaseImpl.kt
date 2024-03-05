package com.lbg.mvvmClean.domain.usecase.catsDetail

import com.lbg.mvvmClean.data.NetworkResult
import com.lbg.mvvmClean.domain.mappers.CallSuccessModel
import com.lbg.mvvmClean.domain.mappers.mapSuccessData
import com.lbg.mvvmClean.domain.repositories.CatDetailsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DeleteFavCatUseCaseImpl(private val catDetailsRepo: CatDetailsRepository) :
    DeleteFavCatUseCase {
    override suspend fun execute(imageId: String, favId: Int) =
        flow<NetworkResult<CallSuccessModel>> {
            emit(NetworkResult.Loading())
            with(catDetailsRepo.deleteFavouriteCatApi(favId)) {
                if (isSuccessful) {
                    emit(NetworkResult.Success(this.body()?.mapSuccessData()))
                    catDetailsRepo.deleteFavouriteCatLocal(imageId)
                } else {
                    emit(NetworkResult.Error(this.errorBody().toString()))
                }
            }

        }.catch {
            emit(NetworkResult.Error(it.localizedMessage))
        }

}

