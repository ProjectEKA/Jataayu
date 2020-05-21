package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import androidx.databinding.ObservableField


class SuccessPageViewModel : BaseViewModel() {
    val fullNameLbl = ObservableField<String>()
    val cmIdInfoLbl = ObservableField<CharSequence>()
}
