package ru.zavodteplictesttask.extensions

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.google.gson.Gson
import retrofit2.Response
import ru.zavodteplictesttask.data.remote.ErrorResponse
import ru.zavodteplictesttask.data.remote.ValidationErrorResponse
import ru.zavodteplictesttask.data.remote.api.ApiResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ContentResolver.getFileName(uri: Uri): String? {
    var fileName: String? = null
    val cursor: Cursor? = this.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1 && it.moveToFirst()) {
            fileName = it.getString(nameIndex)
        }
    }
    return fileName
}

fun String.getZodiacSign(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val birthDate = dateFormat.parse(this)

    val calendar = Calendar.getInstance()
    if (birthDate != null) {
        calendar.time = birthDate
    }

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1

    return when {
        (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Водолей"
        (month == 2 && day >= 19) || (month == 3 && day <= 20) -> "Рыбы"
        (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Овен"
        (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "Телец"
        (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "Близнецы"
        (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "Рак"
        (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "Лев"
        (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "Дева"
        (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "Весы"
        (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Скорпион"
        (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Стрелец"
        (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "Козерог"
        else -> "Неизвестно"
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    val response = apiCall.invoke()
    return if (response.isSuccessful) {
        ApiResult.Success(response.body()!!)
    } else {
        val errorBody = response.errorBody()?.string()
        when (response.code()) {
            404 -> {
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                ApiResult.Error(response.code(), errorResponse?.detail?.message ?: "Not Found")
            }

            422 -> {
                val validationErrorResponse =
                    Gson().fromJson(errorBody, ValidationErrorResponse::class.java)
                val errorMessage =
                    validationErrorResponse?.detail?.joinToString { it.msg ?: "Validation Error" }
                ApiResult.Error(response.code(), errorMessage ?: "Validation Error")
            }

            else -> {
                ApiResult.Error(response.code(), "Something went wrong")
            }
        }
    }
}