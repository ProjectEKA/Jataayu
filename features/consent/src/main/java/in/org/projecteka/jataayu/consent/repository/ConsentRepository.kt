package `in`.org.projecteka.jataayu.consent.repository

import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.remote.ConsentApis
import retrofit2.Call

interface ConsentRepository {
    fun getConsents(): Call<ConsentsListResponse>
}

class ConsentRepositoryImpl(private val consentApi: ConsentApis) : ConsentRepository {
    override fun getConsents(): Call<ConsentsListResponse> {
        return consentApi.getConsents()
    }
}