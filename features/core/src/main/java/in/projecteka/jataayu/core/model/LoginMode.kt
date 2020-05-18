package `in`.projecteka.jataayu.core.model
import com.google.gson.annotations.SerializedName


data class LoginType(@SerializedName("loginType") val loginMode: LoginMode) {}

enum class LoginMode(val loginMode: String) {
    OTP("OTP"), PASSWORD("CREDENTIAL")
}