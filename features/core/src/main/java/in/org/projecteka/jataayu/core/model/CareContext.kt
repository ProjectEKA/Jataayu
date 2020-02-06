package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.BR
import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

open class CareContext(@SerializedName("referenceNumber") open val referenceNumber: String,
                       @SerializedName("display") open val display: String): BaseObservable(), IDataBindingModel {

    var contextChecked = true

    override fun layoutResId(): Int = R.layout.patient_account_result_item

    override fun dataBindingVariable() = BR.careContext
}