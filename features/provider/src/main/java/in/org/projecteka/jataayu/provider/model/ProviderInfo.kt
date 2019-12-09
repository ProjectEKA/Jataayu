package `in`.org.projecteka.jataayu.provider.model

import `in`.org.projecteka.featuresprovider.BR
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding

data class ProviderInfo(
    val city: String,
    val name: String,
    val telephone: String,
    val type: String
): IDataBinding {
    override fun layoutResId(): Int = R.layout.provider_search_result_item

    override fun dataBindingVariable() = BR.providerInfo

    fun nameCityPair() = "$name, $city"
}