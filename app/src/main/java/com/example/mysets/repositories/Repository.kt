package com.example.mysets.repositories

import com.example.mysets.extension.safeApi
import com.example.mysets.network.LegoApiService

class Repository(
    private val legoApiService: LegoApiService
) {

    suspend fun getLegoSetByNumber(setNumber: String) =
        safeApi { legoApiService.getSetByNumber(setNumber).await() }

    suspend fun getSampleLegoSet() = safeApi { legoApiService.getSampleLegoSet().await() }
}