package `in`.projecteka.jataayu.core.model

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName

data class Consent(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("purpose") val purpose: Purpose,
    @SerializedName("patient") val patient: PatientId,
    @SerializedName("hip") val hip: Hip?,
    @SerializedName("hiu") val hiu: HiuRequester,
    @SerializedName("requester") val requester: Requester,
    @SerializedName("hiTypes") val hiTypes: ArrayList<String>,
    @SerializedName("permission") @Bindable val permission: Permission,
    @SerializedName("status") val status: RequestStatus,
    @SerializedName("lastUpdated") val lastUpdated: String
) : BaseObservable(), IDataBindingModel, Cloneable {
    override fun layoutResId(): Int {
        return R.layout.consent_item
    }

    override fun dataBindingVariable(): Int {
        return BR.consent
    }

    fun getPermissionStartDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dateRange.from)
    }

    fun getPermissionEndDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dateRange.to)
    }

    fun getRequestIssueRelativeTimeSpan(context: Context): String {
        var relativeTimeSpan: String
        if (status == RequestStatus.GRANTED){
            relativeTimeSpan = String.format(
                context.getString(R.string.granted_timespan),
                DateTimeUtils.getRelativeTimeSpan(lastUpdated))
        } else{
            relativeTimeSpan = String.format(
                context.getString(R.string.requested_timespan),
                DateTimeUtils.getRelativeTimeSpan(createdAt))
        }
        return relativeTimeSpan
    }

    fun getConsentExpiry(): String {
        return DateTimeUtils.getFormattedDateTime(permission.dataExpiryAt)
    }

    fun getConsentExpiryDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dataExpiryAt)
    }

    fun getConsentExpiryTime(): String {
        return DateTimeUtils.getFormattedTime(permission.dataExpiryAt)
    }

    fun updateFromDate(utcDate: String) {
        permission.dateRange.from = utcDate
    }

    fun updateToDate(utcDate: String) {
        permission.dateRange.to = utcDate
    }

    public override fun clone(): Consent {
        return copy(permission = permission.clone())
    }
}