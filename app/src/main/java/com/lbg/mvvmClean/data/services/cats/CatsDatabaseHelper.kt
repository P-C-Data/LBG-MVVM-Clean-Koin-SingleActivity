package com.lbg.mvvmClean.data.services.cats

import com.lbg.mvvmClean.data.models.catData.FavouriteCatsItem

interface CatsDatabaseHelper {
    suspend fun insertFavCatImageRelation(favCatItems: List<FavouriteCatsItem>): List<Long>
}