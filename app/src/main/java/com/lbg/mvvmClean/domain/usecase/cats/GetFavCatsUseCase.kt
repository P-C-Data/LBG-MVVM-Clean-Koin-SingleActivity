package com.lbg.mvvmClean.domain.usecase.cats

import com.lbg.mvvmClean.data.NetworkResult
import com.lbg.mvvmClean.domain.mappers.CatDataModel
import kotlinx.coroutines.flow.Flow

interface GetFavCatsUseCase {
    suspend fun execute(): Flow<NetworkResult<List<CatDataModel>>>
}