package com.example.mysets.network

sealed class Result<out T : Any> {
    // 200
    data class Success<out T : Any>(val data: T) : Result<T>()
    // 404
    data class Error(val error: String) : Result<Nothing>()
    // 400
    data class Exception(val exception: kotlin.Exception) : Result<Nothing>()

    data class Respond<out T : Any>(val data: T) : Result<T>()
}