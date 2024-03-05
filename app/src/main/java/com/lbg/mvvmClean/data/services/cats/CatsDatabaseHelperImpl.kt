package com.lbg.mvvmClean.data.services.cats

import com.lbg.mvvmClean.data.database.LBGDatabase
import com.lbg.mvvmClean.data.database.entities.FavImageEntity
import com.lbg.mvvmClean.data.models.catData.FavouriteCatsItem

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