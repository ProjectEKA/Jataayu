package `in`.projecteka.jataayu.core.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName

data class Permission (
    @SerializedName("accessMode") val accessMode : String,
    @SerializedName("dateRange") @Bindable val dateRange : DateRange,
    @SerializedName("dataEraseAt") var dataEraseAt : String,
    @SerializedName("frequency") val frequency : Frequency
) : BaseObservable(), Cloneable {
    public override fun clone(): Permission {
        return Permission(accessMode, DateRange(dateRange.from, dateRange.to), dataEraseAt, frequency)
    }
}