package `in`.org.projecteka.jataayu.core.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class DateRange (
    @SerializedName("from") var from : String,
    @SerializedName("to") var to : String
) : BaseObservable()