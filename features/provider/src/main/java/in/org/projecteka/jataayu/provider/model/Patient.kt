package `in`.org.projecteka.jataayu.provider.model

import `in`.org.projecteka.featuresprovider.BR
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import com.squareup.moshi.Json

data class Patient(@field:Json(name = "id") val id : String,
                   @field:Json(name = "name") val name : String,
                   @field:Json(name = "gender") val gender : String,
                   @field:Json(name = "contact") val contact : Int,
                   @field:Json(name = "address") val address : Address): IDataBinding {
    override fun layoutResId(): Int = R.layout.patient_account_result_item

    override fun dataBindingVariable() = BR.patientAccount
}