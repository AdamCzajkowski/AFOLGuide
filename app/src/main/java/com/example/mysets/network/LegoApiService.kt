package com.example.mysets.network

import com.example.mysets.models.ApiResultSearch
import com.example.mysets.models.BrickResult
import com.example.mysets.models.LegoSet
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LegoApiService {
    @GET("sets/{set_num}?")
    fun getSetByNumber(@Path("set_num") setNumber: String): Deferred<Response<LegoSet>>

    @GET("sets/8421-1/")
    fun getSampleLegoSet(): Deferred<Response<LegoSet>>

    @GET("sets/")
    fun getLegoBySearch(@Query("search") searchQuery: String, @Query("page_size") page_size: Int): Deferred<Response<ApiResultSearch>>

    @GET("sets/{set_num}/parts")
    fun getBricks(@Path("set_num") setNumber: String, @Query("page") page: Int): Deferred<Response<BrickResult>>
}