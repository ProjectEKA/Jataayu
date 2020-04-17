package `in`.projecteka.jataayu.presentation

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val showProgress = ObservableBoolean()
    val showProgressMessage = ObservableInt(R.string.default_progress_message)
    val appBarTitle = ObservableField<String>()

}