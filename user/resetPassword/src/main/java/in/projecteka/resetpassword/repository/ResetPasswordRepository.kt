package `in`.projecteka.resetpassword.repository

import `in`.projecteka.resetpassword.remote.ResetPasswordApis
import `in`.projecteka.resetpassword.model.*
import retrofit2.Call

interface ResetPasswordRepository {
    fun verifyOtp(body: VerifyOTPRequest): Call<VerifyOTPResponse>
    fun generateOtp(body: GenerateOTPRequest): Call<GenerateOTPResponse>
    fun resetPassword(body: ResetPasswordRequest, tempToken: String): Call<ResetPasswordResponse>
}

class ResetPasswordRepositoryImpl(private val resetPasswordApis: ResetPasswordApis) : ResetPasswordRepository {
    override fun verifyOtp(body: VerifyOTPRequest): Call<VerifyOTPResponse> {
        return resetPasswordApis.verifyIdentifier(body)
    }

    override fun generateOtp(body: GenerateOTPRequest): Call<GenerateOTPResponse> {
        return resetPasswordApis.generateOtp(body)
    }

    override fun resetPassword(body: ResetPasswordRequest, tempToken: String): Call<ResetPasswordResponse> {
        return resetPasswordApis.resetPassword(body, tempToken)
    }
}
