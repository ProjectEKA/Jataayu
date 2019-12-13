package `in`.org.projecteka.jataayu.provider.ui.adapter

import `in`.org.projecteka.jataayu.provider.domain.ProviderNameWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @BindingAdapter("app:onValueChanged")
    @JvmStatic
    fun onValueChanged(editText : EditText, providerNameWatcher : ProviderNameWatcher) {
         editText.addTextChangedListener(providerNameWatcher)
    }

    @BindingAdapter("app:toggledVisibility")
    @JvmStatic
    fun conditionalVisibility(view : View, shouldShow : Boolean) {
         view.visibility = if (shouldShow) VISIBLE else GONE
    }

    @BindingAdapter("app:selected")
    @JvmStatic
    fun setSelected(view : View, isSelected : Boolean) {
         view.isSelected = isSelected
    }
}