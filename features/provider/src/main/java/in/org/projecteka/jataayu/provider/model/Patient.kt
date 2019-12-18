package `in`.org.projecteka.jataayu.provider.model

import `in`.org.projecteka.featuresprovider.BR
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import com.google.gson.annotations.SerializedName

data class Patient(@SerializedName("id") val id : String,
                   @SerializedName("name") val name : String,
                   @SerializedName("gender") val gender : String,
                   @SerializedName("contact") val contact : Int,
                   @SerializedName("address") val address : Address): IDataBinding {
    override fun layoutResId(): Int = R.layout.patient_account_result_item

    override fun dataBindingVariable() = BR.patientAccount
}