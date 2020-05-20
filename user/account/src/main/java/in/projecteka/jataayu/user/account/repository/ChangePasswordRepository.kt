package `in`.projecteka.jataayu.user.account.repository

import `in`.projecteka.jataayu.user.account.model.ChangePasswordRequest
import `in`.projecteka.jataayu.user.account.model.ChangePasswordResponse
import `in`.projecteka.jataayu.user.account.remote.ChangePasswordApis
import retrofit2.Call

interface ChangePasswordRepository {
    fun changePassword(body: ChangePasswordRequest): Call<ChangePasswordResponse>
}

class ChangePasswordRepositoryImpl(private val changePasswordApis: ChangePasswordApis) : ChangePasswordRepository {
    override fun changePassword(body: ChangePasswordRequest): Call<ChangePasswordResponse> {
        return changePasswordApis.changePassword(body)
    }
}
