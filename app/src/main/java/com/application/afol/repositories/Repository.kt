package com.application.afol.repositories

import com.application.afol.database.MySetsDatabase
import com.application.afol.extension.safeApi
import com.application.afol.models.Label
import com.application.afol.models.LegoSet
import com.application.afol.network.LegoApiService

class Repository(
    private val legoApiService: LegoApiService,
    private val mySetsDatabase: MySetsDatabase
) {
    suspend fun getLegoBySearch(searchQuery: String, pageSize: Int, page: Int) =
        safeApi { legoApiService.getLegoBySearchAsync(searchQuery, page, pageSize).await() }

    fun getFavorites() = mySetsDatabase.mySetsDao().getAll()

    fun addToFavorites(legoSet: LegoSet) = mySetsDatabase.mySetsDao().insert(legoSet)

    fun removeFromFavorites(legoSet: LegoSet) = mySetsDatabase.mySetsDao().delete(legoSet)

    fun addLabel(legoSet: LegoSet, label: Label) = legoSet.listOfLabels?.add(label).also {
        mySetsDatabase.mySetsDao().update(legoSet)
    }

    fun removeLabel(legoSet: LegoSet, label: Label) = legoSet.listOfLabels?.forEach {
        if (it == label) {
            legoSet.listOfLabels.remove(label)
            mySetsDatabase.mySetsDao().update(legoSet)
        }
    }

    suspend fun getBricksFromSet(page: Int, setNumber: String, pageSize: Int) =
        safeApi { legoApiService.getBricksAsync(setNumber, page, pageSize).await() }

    suspend fun getLegoAlternatives(setNumber: String) =
        safeApi { legoApiService.getMOCsAsync(setNumber).await() }

    suspend fun getPartBySearch(page: Int, pageSize: Int, searchQuery: String) =
        safeApi { legoApiService.getPartBySearchAsync(page, pageSize, searchQuery).await() }
}