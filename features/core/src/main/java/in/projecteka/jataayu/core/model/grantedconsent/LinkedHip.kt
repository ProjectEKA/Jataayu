package `in`.projecteka.jataayu.core.model.grantedconsent

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel

data class LinkedHip(
    val providerName: String,
    val patientReferenceId: String?
) : IDataBindingModel {

    override fun layoutResId(): Int {
        return R.layout.linked_hip_item
    }

    override fun dataBindingVariable(): Int {
        return BR.linkedHip
    }
}