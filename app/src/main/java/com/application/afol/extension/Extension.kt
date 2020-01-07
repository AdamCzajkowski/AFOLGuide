package com.application.afol.extension

import android.util.Log
import com.application.afol.network.Result
import retrofit2.Response

suspend fun <T : Any> safeApi(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call.invoke()

        if (response.isSuccessful) return Result.Success(response.body()!!)
        else
            Log.i("test", "error ->>>>>>>>>> ${response.raw()}")
        return Result.Error(response.raw().toString())
    } catch (e: Exception) {
        return Result.Exception(e)
    }
}
