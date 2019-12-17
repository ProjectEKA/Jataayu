package `in`.org.projecteka.jataayu.provider.model

import `in`.org.projecteka.featuresprovider.BR
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import com.squareup.moshi.Json

data class ProviderInfo(
    @field:Json(name="city") val city: String,
    @field:Json(name="name") val name: String,
    @field:Json(name="telephone") val telephone: String,
    @field:Json(name="type") val type: String
): IDataBinding {
    override fun layoutResId(): Int = R.layout.provider_search_result_item

    override fun dataBindingVariable() = BR.providerInfo

    fun nameCityPair() = "$name, $city"
}