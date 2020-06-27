package `in`.projecteka.jataayu.authorization.remote

import model.CreateAccountResponse
import model.RefreshTokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("sessions")
    fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<CreateAccountResponse>
}