package `in`.projecteka.jataayu.core.model

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

open class CareContext(@SerializedName("referenceNumber") open val referenceNumber: String,
                       @SerializedName("display") open val display: String): BaseObservable(), IDataBindingModel {

    var contextChecked = true

    override fun layoutResId(): Int = R.layout.patient_account_result_item

    override fun dataBindingVariable() = BR.careContext
}