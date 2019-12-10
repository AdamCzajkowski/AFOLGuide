package com.application.afol.network

import com.application.afol.models.ApiResultSearch
import com.application.afol.models.BrickResult
import com.application.afol.models.MOCResult
import com.application.afol.models.PartResult
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LegoApiService {
    @GET("sets/")
    fun getLegoBySearchAsync(@Query("search") searchQuery: String, @Query("page") page: Int, @Query("page_size") page_size: Int): Deferred<Response<ApiResultSearch>>

    @GET("sets/{set_num}/parts")
    fun getBricksAsync(@Path("set_num") setNumber: String, @Query("page") page: Int, @Query("page_size") page_size: Int): Deferred<Response<BrickResult>>

    @GET("sets/{set_num}/alternates/")
    fun getMOCsAsync(@Path("set_num") setNumber: String): Deferred<Response<MOCResult>>

    @GET("parts/")
    fun getPartBySearchAsync(@Query("page") page: Int, @Query("page_size") pageSize: Int, @Query("search") searchQuery: String): Deferred<Response<PartResult>>

    @GET("sets/")
    fun getLegoByThemeAsync(@Query("page") page: Int, @Query("page_size") page_size: Int, @Query("theme_id") theme_id: Int): Deferred<Response<ApiResultSearch>>
}