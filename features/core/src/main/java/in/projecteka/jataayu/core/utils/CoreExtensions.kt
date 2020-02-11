package `in`.projecteka.jataayu.core.utils

import `in`.projecteka.jataayu.core.model.ErrorResponse
import `in`.projecteka.jataayu.util.extension.fromJson
import com.google.gson.Gson
import okhttp3.ResponseBody

fun ResponseBody.toErrorResponse() : ErrorResponse {
    return Gson().fromJson<ErrorResponse>(string()!!)
}