package `in`.projecteka.jataayu.provider.domain

import `in`.projecteka.jataayu.provider.callback.TextWatcherCallback
import android.text.Editable
import android.text.TextWatcher
import android.view.View

class ProviderNameWatcher(private val textWatcherCallback : TextWatcherCallback,
                          private val searchThreshold : Int) : TextWatcher {
    override fun afterTextChanged(s : Editable?) {}
    override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}
    override fun onTextChanged(query : CharSequence?, start : Int, before : Int, count : Int) {
        if (hasValidSearchQuery(query)) textWatcherCallback.onTextChanged(query, View.VISIBLE)
        else textWatcherCallback.onTextCleared(View.GONE)
    }

    private fun hasValidSearchQuery(query : CharSequence?) =
        query != null && query.trim().isNotEmpty() && query.length >= searchThreshold
}