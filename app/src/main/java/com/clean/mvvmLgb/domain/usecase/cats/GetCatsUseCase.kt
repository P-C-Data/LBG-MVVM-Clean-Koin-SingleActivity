package com.clean.mvvmLgb.domain.usecase.cats

import com.clean.mvvmLgb.data.NetworkResult
import com.clean.mvvmLgb.domain.mappers.CatDataModel
import kotlinx.coroutines.flow.Flow

interface GetCatsUseCase {
    suspend fun execute(): Flow<NetworkResult<List<CatDataModel>>>
}