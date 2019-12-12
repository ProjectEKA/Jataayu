package `in`.org.projecteka.jataayu.provider.ui.adapter

import `in`.org.projecteka.jataayu.provider.domain.ProviderNameWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @BindingAdapter("app:onValueChanged")
    @JvmStatic
    fun onValueChanged(editText : EditText, providerNameWatcher : ProviderNameWatcher) {
         editText.addTextChangedListener(providerNameWatcher)
    }
}