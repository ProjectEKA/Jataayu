package `in`.projecteka.jataayu.network.authenticator

import `in`.projecteka.jataayu.authorization.remote.RefreshTokenApi
import `in`.projecteka.jataayu.network.utils.NetworkManager
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import model.CreateAccountResponse
import model.RefreshTokenRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route

class TokenAuthenticator(private val networkManger: NetworkManager): Authenticator {

    private val authService: RefreshTokenApi
    get() = networkManger.retrofit.create(RefreshTokenApi::class.java)
    private val credentialsRepository: CredentialsRepository
    get() = networkManger.credentialsRepository


    private fun getNewToken(): CreateAccountResponse? {
        val refreshTokenResponse = authService.refreshToken(getRefreshTokenRequestPayload()).execute()
        return refreshTokenResponse.body()
    }

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
        return getNewToken()?.let {
            credentialsRepository.accessToken =
                "${it.tokenType.capitalize()} ${it.accessToken}"
            credentialsRepository.refreshToken = it.refreshToken
            return response.request.newBuilder()
                .header("Authorization", credentialsRepository.accessToken!!)
                .build()
        }
    }

    private fun getRefreshTokenRequestPayload(): RefreshTokenRequest {
        return RefreshTokenRequest(networkManger.preferenceRepository.consentManagerId!!, "refreshToken", networkManger.credentialsRepository.refreshToken!!)
    }
}