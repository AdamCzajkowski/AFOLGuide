package com.example.mysets.extension

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mysets.network.Result
import retrofit2.Response
import java.lang.Exception

suspend fun <T : Any> safeApi(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call.invoke()

        if (response.isSuccessful) return Result.Success(response.body()!!)
        else
            Log.i("test", "error ->>>>>>>>>> ${response.raw()}")
            return Result.Error(response.errorBody().toString())
    } catch (e: Exception) {
        return Result.Exception(e)
    }
}

fun ViewGroup.inflate(): LayoutInflater = LayoutInflater.from(context)
