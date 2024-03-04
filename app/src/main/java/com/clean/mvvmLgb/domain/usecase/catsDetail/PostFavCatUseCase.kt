package com.clean.mvvmLgb.domain.usecase.catsDetail

import com.clean.mvvmLgb.data.NetworkResult
import com.clean.mvvmLgb.domain.mappers.CallSuccessModel
import kotlinx.coroutines.flow.Flow

interface PostFavCatUseCase {
    suspend fun execute(imageId: String): Flow<NetworkResult<CallSuccessModel>>
}