package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.BR
import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel

data class LinkedAccount(
    val providerName: String,
    val patientReferenceId: String?,
    val patientName: String?) : IDataBindingModel {
    override fun layoutResId(): Int {
        return R.layout.account_item
    }

    override fun dataBindingVariable(): Int {
        return BR.linkedAccount
    }
}