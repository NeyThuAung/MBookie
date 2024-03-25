package com.example.mbookie.util

sealed class UiState<out T> {

    object Loading : UiState<Nothing>()
    data class Success<out T>(val data : T): UiState<T>()
    data class Failure(val error: String): UiState<Nothing>()

}


sealed class MovieNetworkResult<T>(
    val data: T? = null,
    val message: ErrorMessage? = null
) {
    class Success<T>(data: T?=null) : MovieNetworkResult<T>(data)
    class Error<T>(message: ErrorMessage,data: T? = null) : MovieNetworkResult<T>(data, message)
    class Loading<T> : MovieNetworkResult<T>()
}

data class ErrorMessage(
    val message: String="",
    val statusCode:Int = 0
)