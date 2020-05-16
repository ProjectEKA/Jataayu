package `in`.projecteka.jataayu.presentation.adapter

import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.google.android.material.textfield.TextInputEditText

object BindingAdapters {

    @BindingAdapter("app:toggledVisibility")
    @JvmStatic
    fun conditionalVisibility(view: View, shouldShow: Boolean) {
        view.visibility = if (shouldShow) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:selected")
    @JvmStatic
    fun setSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected
    }

    @BindingAdapter("app:onNavigationItemSelected")
    @JvmStatic
    fun setOnNavigationItemSelectedListener(view: BottomNavigationView, listener: OnNavigationItemSelectedListener) {
        view.setOnNavigationItemSelectedListener(listener)
    }

    @BindingAdapter("app:onItemSelected")
    @JvmStatic
    fun onItemSelected(spinner: AppCompatSpinner, onItemSelectedListener: AdapterView.OnItemSelectedListener) {
        spinner.onItemSelectedListener = onItemSelectedListener
    }

    @BindingAdapter("app:onValueChanged")
    @JvmStatic
    fun onValueChanged(editText: EditText, textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
    }

    @JvmStatic
    @BindingAdapter("app:resource_reference")
    fun bindStringResourceIdToString(textView: TextView, @StringRes id: Int) {
        val text = textView.context.getString(id)
        textView.text = text
    }

    @JvmStatic
    @BindingAdapter("toggle_error", "error_text", requireAll = true)
    fun bindErrorToInput(editText: TextInputEditText, toggleError: Boolean, errorText: String) {
        if (toggleError)
            editText.error = errorText
        else editText.error = null
    }

    @BindingAdapter("imageResource")
    @JvmStatic
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }
}