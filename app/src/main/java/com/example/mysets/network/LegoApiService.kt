package com.example.mysets.network

import com.example.mysets.models.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LegoApiService {
    @GET("sets/")
    fun getLegoBySearch(@Query("search") searchQuery: String, @Query("page") page: Int, @Query("page_size") page_size: Int): Deferred<Response<ApiResultSearch>>

    @GET("sets/{set_num}/parts")
    fun getBricks(@Path("set_num") setNumber: String, @Query("page") page: Int, @Query("page_size") page_size: Int): Deferred<Response<BrickResult>>

    @GET("sets/{set_num}/alternates/")
    fun getMOCs(@Path("set_num") setNumber: String): Deferred<Response<MOCResult>>

    @GET("sets/")
    fun getLegoDetail(@Query("set_num") setNumber: String): Deferred<Response<LegoSet>>

    @GET("themes/")
    fun getThemes(@Query("page") page: Int, @Query("page_size") pageSize: Int): Deferred<Response<ThemesResult>>
}