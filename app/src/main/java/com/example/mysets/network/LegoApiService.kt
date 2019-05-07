package com.example.mysets.network

import com.example.mysets.models.LegoSet
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LegoApiService {
    @GET ("sets/{set_num}?")
    fun getSetByNumber(@Path("set_num") setNumber: String): Deferred<Response<LegoSet>>

    @GET ("sets/8421-1/")
    fun getSampleLegoSet(): Deferred<Response<LegoSet>>
}