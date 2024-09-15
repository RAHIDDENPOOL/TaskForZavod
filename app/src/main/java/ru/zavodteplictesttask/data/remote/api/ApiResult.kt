package ru.zavodteplictesttask.data.remote.api

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val errorMessage: String) : ApiResult<Nothing>()
}