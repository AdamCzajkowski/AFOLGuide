package com.application.afol.repositories

import com.application.afol.database.MySetsDatabase
import com.application.afol.extension.safeApi
import com.application.afol.models.LegoSet
import com.application.afol.network.LegoApiService

class Repository(
    private val legoApiService: LegoApiService,
    private val mySetsDatabase: MySetsDatabase
) {
    suspend fun getLegoBySearch(searchQuery: String, pageSize: Int, page: Int) =
        safeApi { legoApiService.getLegoBySearch(searchQuery, page, pageSize).await() }

    fun getMySets() = mySetsDatabase.mySetsDao().getAll()

    fun addToMySets(legoSet: LegoSet) = mySetsDatabase.mySetsDao().insert(legoSet)

    fun removeFromMySets(legoSet: LegoSet) = mySetsDatabase.mySetsDao().delete(legoSet)

    suspend fun getBricksFromSet(page: Int, setNumber: String, pageSize: Int) =
        safeApi { legoApiService.getBricks(setNumber, page, pageSize).await() }

    /*suspend fun getLegoDetail(setNumber: String) =
        safeApi { legoApiService.getLegoDetail(setNumber).await() }*/

    suspend fun getLegoAlternatives(setNumber: String) =
        safeApi { legoApiService.getMOCs(setNumber).await() }

    suspend fun getThemes(page: Int, pageSize: Int) =
        safeApi { legoApiService.getThemes(page, pageSize).await() }

    suspend fun getLegoFromTheme(page: Int, pageSize: Int, themeId: Int) =
        safeApi { legoApiService.getLegoByTheme(page, pageSize, themeId).await() }

    suspend fun getPartBySearch(page: Int, pageSize: Int, searchQuery: String) =
        safeApi { legoApiService.getPartBySearch(page, pageSize, searchQuery).await() }
}