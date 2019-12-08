package `in`.org.projecteka.jataayu.viewmodel

import `in`.org.projecteka.jataayu.datasource.model.SampleModel
import `in`.org.projecteka.jataayu.repository.SampleRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LauncherViewModel(private val sampleRepository: SampleRepository) : ViewModel() {
    var sampleModel : MutableLiveData<SampleModel> = MutableLiveData()

    internal fun getLabel() {
        sampleRepository.getSampleText(id = 1).enqueue(object : Callback<SampleModel> {
            override fun onFailure(call: Call<SampleModel>, t: Throwable) {
                onResponse(call, Response.success(SampleModel(title = t.localizedMessage)))
            }

            override fun onResponse(call: Call<SampleModel>, response: Response<SampleModel>) {
                if (response.body() != null) {
                    sampleModel.value = response.body()
                }
            }
        })
    }
}