package `in`.org.projecteka.jataayu.provider.model

import `in`.org.projecteka.featuresprovider.BR
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import com.google.gson.annotations.SerializedName

data class CareContext(@SerializedName("referenceNumber") val referenceNumber : String,
                                    @SerializedName("display") val display : String): IDataBinding {
    override fun layoutResId(): Int = R.layout.patient_account_result_item

    override fun dataBindingVariable() = BR.careContext
}