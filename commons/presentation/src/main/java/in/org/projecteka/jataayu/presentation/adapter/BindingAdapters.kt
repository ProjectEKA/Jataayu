package `in`.org.projecteka.jataayu.presentation.adapter

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener

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



}