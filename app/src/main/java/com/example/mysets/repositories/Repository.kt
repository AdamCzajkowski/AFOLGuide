package com.example.mysets.repositories

import com.example.mysets.database.my.sets.MySetsDatabase
import com.example.mysets.database.wishlist.WishlistDatabase
import com.example.mysets.extension.safeApi
import com.example.mysets.models.LegoSet
import com.example.mysets.network.LegoApiService

class Repository(
    private val legoApiService: LegoApiService,
    private val mySetsDatabase: MySetsDatabase,
    private val wishlistDatabase: WishlistDatabase
) {
    suspend fun getLegoSetByNumber(setNumber: String) =
        safeApi { legoApiService.getSetByNumber(setNumber).await() }

    suspend fun getLegoBySearch(searchQuery: String, pageSize: Int) =
        safeApi { legoApiService.getLegoBySearch(searchQuery, pageSize).await() }

    suspend fun getSampleLegoSet() = safeApi { legoApiService.getSampleLegoSet().await() }

    fun getMySets() = mySetsDatabase.mySetsDao().getAll()

    fun getWishlist() = wishlistDatabase.wishlistDao().getAll()

    fun addToMySets(legoSet: LegoSet) = mySetsDatabase.mySetsDao().insert(legoSet)

    fun addToWishlist(legoSet: LegoSet) = wishlistDatabase.wishlistDao().insert(legoSet)
}