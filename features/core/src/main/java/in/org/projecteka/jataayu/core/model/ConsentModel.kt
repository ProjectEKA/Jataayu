package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.BR
import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import com.google.gson.annotations.SerializedName

data class ConsentModel (

    @SerializedName("requestedTimestamp") val requestedTimestamp : String,
    @SerializedName("hiu") val hiu : Hiu,
    @SerializedName("purpose") val purpose : String,
    @SerializedName("requestInfo") val requestInfo : RequestInfo,
    @SerializedName("accounts") val accounts : List<Accounts>,
    @SerializedName("consentExpiry") val consentExpiry : String,
    @SerializedName("status") val status : String
): IDataBindingModel {
    override fun layoutResId(): Int {
        return R.layout.consent_item
    }

    override fun dataBindingVariable(): Int {
        return BR.consent
    }
}