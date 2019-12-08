package `in`.org.projecteka.jataayu.repository

import `in`.org.projecteka.jataayu.datasource.api.SampleApi
import `in`.org.projecteka.jataayu.datasource.model.SampleModel
import retrofit2.Call

interface SampleRepository {
    fun getSampleText(id: Int): Call<SampleModel>
}

class SampleRepositoryImpl(private val sampleApi: SampleApi) : SampleRepository {
    override fun getSampleText(id: Int): Call<SampleModel> {
        return sampleApi.getResponse(id)
    }
}