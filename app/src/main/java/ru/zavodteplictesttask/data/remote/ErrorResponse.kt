package ru.zavodteplictesttask.data.remote

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("detail")
    val detail: DetailResponse?
)

data class DetailResponse(
    @SerializedName("message")
    val message: String?
)

data class ValidationErrorResponse(
    @SerializedName("detail")
    val detail: List<ValidationError>?
)

data class ValidationError(
    @SerializedName("loc")
    val loc: List<String>?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("type")
    val type: String?
)
