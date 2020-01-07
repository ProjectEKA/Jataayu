package `in`.org.projecteka.jataayu.consent.ui.adapter

import `in`.org.projecteka.jataayu.provider.domain.ProviderNameWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter



object ProviderBindingAdapters {
    @BindingAdapter("app:onValueChanged")
    @JvmStatic
    fun onValueChanged(editText: EditText, providerNameWatcher: ProviderNameWatcher) {
        editText.addTextChangedListener(providerNameWatcher)
    }

}