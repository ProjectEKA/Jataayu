package `in`.projecteka.jataayu.user.account.remote


import `in`.projecteka.jataayu.user.account.model.ChangePasswordRequest
import `in`.projecteka.jataayu.user.account.model.ChangePasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface ChangePasswordApis {
    @PUT("patients/profile/update-password")
    fun changePassword(@Body body: ChangePasswordRequest): Call<ChangePasswordResponse>
}
