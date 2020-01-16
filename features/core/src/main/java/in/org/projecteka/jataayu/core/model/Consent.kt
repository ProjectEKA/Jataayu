package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.BR
import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.Context
import com.google.gson.annotations.SerializedName


data class Consent(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("purpose") val purpose: Purpose,
    @SerializedName("hip") val hip: Hip,
    @SerializedName("hiu") val hiu: HiuRequester,
    @SerializedName("requester") val requester: Requester,
    @SerializedName("hiTypes") val hiTypes: List<HiTypes>,
    @SerializedName("permission") val permission: Permission,
    @SerializedName("status") val status: RequestStatus
) : IDataBindingModel {
    override fun layoutResId(): Int {
        return R.layout.consent_item
    }

    override fun dataBindingVariable(): Int {
        return BR.consent
    }

    fun getPermissionStartDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dateRange.from)
    }

    fun getPermissionToDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dateRange.to)
    }

    fun getRequestIssueRelativeTimeSpan(context: Context): String {
        return String.format(context.getString(R.string.requested_timespan), DateTimeUtils.getRelativeTimeSpan(createdAt))
    }

    fun getConsentExpiry(): String {
        return DateTimeUtils.getFormattedDateTime(permission.dataExpiryAt)
    }
}