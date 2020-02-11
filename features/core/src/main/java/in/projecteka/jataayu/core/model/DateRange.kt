package `in`.projecteka.jataayu.core.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName

data class DateRange (
    @SerializedName("from") @Bindable var from : String,
    @SerializedName("to") @Bindable var to : String
) : BaseObservable()