package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.BR
import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class CareContext(@SerializedName("referenceNumber") val referenceNumber : String,
                                    @SerializedName("display") val display : String): BaseObservable(), IDataBindingModel {

    public var contextChecked : Boolean = false

    public fun setChecked(checked: Boolean) {
        this.contextChecked = checked
        notifyPropertyChanged(BR.careContext)
    }

    override fun layoutResId(): Int = R.layout.patient_account_result_item

    override fun dataBindingVariable() = BR.careContext
}