package com.clean.mvvmLgb.data.services.cats

import com.clean.mvvmLgb.data.database.LBGDatabase
import com.clean.mvvmLgb.data.database.entities.FavImageEntity
import com.clean.mvvmLgb.data.models.catData.FavouriteCatsItem

class CatsDatabaseHelperImpl(private val db: LBGDatabase) : CatsDatabaseHelper {
    override suspend fun insertFavCatImageRelation(favCatItems: List<FavouriteCatsItem>): List<Long> {
        val favCatRelList = favCatItems.map {
            FavImageEntity(
                favouriteId = it.id,
                imageId = it.imageId
            )
        }
        return favCatRelList.let { db.favImageDao().insertFavCatImageRelation(favCatRelList) }
    }
}