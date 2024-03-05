package com.lbg.mvvmClean.data.services.catsDetail

interface CatsDetailsDatabaseHelper {
    suspend fun insertFavCatImageRelation(favCatId: Int, imageId: String): Long
    suspend fun deleteFavImage(catImageId: String): Int
    suspend fun isFavourite(catImageId: String): Int?

}