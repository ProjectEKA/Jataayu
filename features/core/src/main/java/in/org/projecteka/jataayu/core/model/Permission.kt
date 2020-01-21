package `in`.org.projecteka.jataayu.core.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class Permission (
    @SerializedName("accessMode") val accessMode : String,
    @SerializedName("dateRange") val dateRange : DateRange,
    @SerializedName("dataExpiryAt") var dataExpiryAt : String,
    @SerializedName("frequency") val frequency : Frequency
) : BaseObservable()