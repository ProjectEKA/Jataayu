package `in`.projecteka.jataayu.presentation.ui.viewmodel

import `in`.projecteka.jataayu.presentation.R
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val showProgress = ObservableBoolean()
    val showProgressMessage = ObservableInt(R.string.default_progress_message)
    val appBarTitle = ObservableField<String>()

    fun showProgress(isLoading: Boolean = true, @StringRes message: Int = R.string.default_progress_message) {
        showProgress.set(isLoading)
        showProgressMessage.set(message)
    }

}