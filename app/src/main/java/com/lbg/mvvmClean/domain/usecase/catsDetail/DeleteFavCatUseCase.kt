package com.lbg.mvvmClean.domain.usecase.catsDetail

import com.lbg.mvvmClean.data.NetworkResult
import com.lbg.mvvmClean.domain.mappers.CallSuccessModel
import kotlinx.coroutines.flow.Flow

interface DeleteFavCatUseCase {
    suspend fun execute(imageId: String, favId: Int): Flow<NetworkResult<CallSuccessModel>>
}