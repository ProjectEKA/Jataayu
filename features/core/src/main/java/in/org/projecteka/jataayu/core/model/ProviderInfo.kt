package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.BR
import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import com.google.gson.annotations.SerializedName

data class ProviderInfo(
    @SerializedName("city") val city: String,
    @SerializedName("identifier") val hip: Hip,
    @SerializedName("telephone") val telephone: String,
    @SerializedName("type") val type: String
) : IDataBindingModel {
    override fun layoutResId(): Int = R.layout.provider_search_result_item

    override fun dataBindingVariable() = BR.providerInfo

    fun nameCityPair() = "${hip.name}, $city"
}