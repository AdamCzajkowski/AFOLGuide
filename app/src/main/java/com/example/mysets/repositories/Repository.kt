package com.example.mysets.repositories

import com.example.mysets.database.my.sets.MySetsDatabase
import com.example.mysets.extension.safeApi
import com.example.mysets.models.LegoSet
import com.example.mysets.network.LegoApiService

class Repository(
    private val legoApiService: LegoApiService,
    private val mySetsDatabase: MySetsDatabase
) {
    suspend fun getLegoBySearch(searchQuery: String, pageSize: Int) =
        safeApi { legoApiService.getLegoBySearch(searchQuery, pageSize).await() }

    fun getMySets() = mySetsDatabase.mySetsDao().getAll()

    fun addToMySets(legoSet: LegoSet) = mySetsDatabase.mySetsDao().insert(legoSet)

    fun removeFromMySets(legoSet: LegoSet) = mySetsDatabase.mySetsDao().delete(legoSet)

    suspend fun getBricksFromSet(page: Int, setNumber: String) =
        safeApi { legoApiService.getBricks(setNumber, page).await() }
}