package `in`.projecteka.jataayu.core.model

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.core.model.RequestStatus.*
import `in`.projecteka.jataayu.core.model.approveconsent.CareReference
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentManager
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import androidx.annotation.StringRes
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import java.util.*

data class Consent(
    @SerializedName("id", alternate = ["consentId"]) val id: String,
    @SerializedName("createdAt") private val createdAt: String,
    @SerializedName("purpose") val purpose: Purpose,
    @SerializedName("patient") val patient: PatientId,
    @SerializedName("hip") val hip: Hip?,
    @SerializedName("hiu") var hiu: HiuRequester,
    @SerializedName("requester") val requester: Requester,
    @SerializedName("hiTypes") val hiTypes: ArrayList<String>,
    @SerializedName("permission") @Bindable val permission: Permission,
    @SerializedName("status") var status: RequestStatus,
    @SerializedName("lastUpdated") private var lastUpdated: String,
    @SerializedName("careContexts") val careContexts : List<CareReference>?,
    @SerializedName("consentManager") val consentManager: ConsentManager? = null
    ) : BaseObservable(), IDataBindingModel, Cloneable {
    var showDetails = true
    lateinit var relativeTimeSpan : String
    @StringRes var baseString = R.string.requested_timespan

    override fun layoutResId(): Int {
        return R.layout.consent_item
    }

    override fun dataBindingVariable(): Int {
        modifyData()
        return BR.consent
    }

    fun getPermissionStartDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dateRange.from)
    }

    fun getPermissionEndDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dateRange.to)
    }

    fun getConsentExpiry(): String {
        return DateTimeUtils.getFormattedDateTime(permission.dataEraseAt)
    }

    fun getConsentExpiryDate(): String {
        return DateTimeUtils.getFormattedDate(permission.dataEraseAt)
    }

    fun getConsentExpiryTime(): String {
        return DateTimeUtils.getFormattedTime(permission.dataEraseAt)
    }

    fun updateFromDate(utcDate: String) {
        permission.dateRange.from = utcDate
    }

    fun updateToDate(utcDate: String) {
        permission.dateRange.to = utcDate
    }

    fun getLastUpdated(): Date? {
        return DateTimeUtils.getDateInUTCFormat(lastUpdated)
    }


    public override fun clone(): Consent {
        return copy(permission = permission.clone())
    }


    private fun modifyData() {

        when(status) {
            GRANTED -> {
                relativeTimeSpan = DateTimeUtils.getRelativeTimeSpan(lastUpdated)
                showDetails = false
                baseString = R.string.granted_timespan
            }
            REVOKED -> {
                relativeTimeSpan = DateTimeUtils.getRelativeTimeSpan(lastUpdated)
                showDetails = true
                baseString = R.string.revoked_timespan
            }
            EXPIRED -> {
                relativeTimeSpan = DateTimeUtils.getRelativeTimeSpan(lastUpdated)
                showDetails = true
                baseString = R.string.expired_timespan
            }
            DENIED -> {
                relativeTimeSpan = DateTimeUtils.getRelativeTimeSpan(lastUpdated)
                showDetails = true
                baseString = R.string.denied_timespan
            }
            else -> {
                createdAt.let { relativeTimeSpan = DateTimeUtils.getRelativeTimeSpan(lastUpdated) }
                showDetails = true
                baseString = R.string.requested_timespan
            }
        }
    }

    companion object  {
        fun getNoConsentMessageResourceId(status: RequestStatus, consentType: ConsentType): Int {
            return when(status) {
                GRANTED -> {
                    R.string.no_granted_consents
                }
                REVOKED -> {
                    R.string.no_revoked_consents
                }
                EXPIRED -> {
                    when(consentType) {
                        ConsentType.REQUESTS -> R.string.no_expired_consent_requests
                        ConsentType.ARTIFACTS -> R.string.no_expired_consents
                    }
                }
                DENIED -> {
                    R.string.no_denied_consents
                }
                REQUESTED -> {
                    R.string.no_new_consent_requests
                }
                ALL -> {
                    when(consentType) {
                        ConsentType.REQUESTS -> R.string.no_consent_requests
                        ConsentType.ARTIFACTS -> R.string.no_consents
                    }
                }
            }
        }
    }
}

enum class ConsentType {
    REQUESTS,
    ARTIFACTS
}