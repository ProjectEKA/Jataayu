package `in`.projecteka.jataayu.core.model

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

open class CareContext(@SerializedName("referenceNumber", alternate = ["careContextReference"]) open val referenceNumber: String,
                       @SerializedName("display", alternate = ["patientReference"]) open var display: String?): BaseObservable(), IDataBindingModel {

    var contextChecked: Boolean? = true

    override fun layoutResId(): Int = R.layout.patient_account_result_item

    override fun dataBindingVariable() = BR.careContext
}