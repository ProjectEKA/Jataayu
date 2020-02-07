package `in`.org.projecteka.jataayu.network.utils

import okhttp3.ResponseBody

interface ResponseCallback {
    fun <T> onSuccess(body: T?)
    fun onFailure(errorBody: ResponseBody)
    fun onFailure(t: Throwable)
}