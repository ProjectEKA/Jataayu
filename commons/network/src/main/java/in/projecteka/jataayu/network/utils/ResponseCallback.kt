package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.model.ErrorResponse

interface ResponseCallback {
    fun <T> onSuccess(body: T?)
    fun onFailure(errorBody: ErrorResponse)
    fun onFailure(t: Throwable)
}