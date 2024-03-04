package com.clean.mvvmLgb.data.services.cats

import com.clean.mvvmLgb.data.models.catData.FavouriteCatsItem

interface CatsDatabaseHelper {
    suspend fun insertFavCatImageRelation(favCatItems: List<FavouriteCatsItem>): List<Long>
}