package `in`.projecteka.jataayu.core.adapter

import `in`.projecteka.jataayu.util.extension.getString
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

object CoreBindingAdapters {
    @BindingAdapter(value = ["app:baseString", "app:text"], requireAll = true)
    @JvmStatic
    fun setText(textView: TextView, @StringRes baseResId: Int, dateTimeSpan: String) {
        textView.text = String.format(textView.getString(baseResId), dateTimeSpan)
    }
}