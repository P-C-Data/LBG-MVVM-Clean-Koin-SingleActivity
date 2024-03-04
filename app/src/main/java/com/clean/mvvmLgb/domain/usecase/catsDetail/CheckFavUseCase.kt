package com.clean.mvvmLgb.domain.usecase.catsDetail

import kotlinx.coroutines.flow.Flow

interface CheckFavUseCase {
    suspend fun execute(imageId: String): Flow<Int?>
}