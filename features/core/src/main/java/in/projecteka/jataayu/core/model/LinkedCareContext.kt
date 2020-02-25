package `in`.projecteka.jataayu.core.model

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import com.google.gson.annotations.SerializedName

data class LinkedCareContext(
    @SerializedName("referenceNumber") val referenceNumber: String,
    @SerializedName("display") val display: String
) :
   IDataBindingModel {

    override fun layoutResId(): Int = R.layout.linked_account_result_item

    override fun dataBindingVariable() = BR.linkedCareContext
}