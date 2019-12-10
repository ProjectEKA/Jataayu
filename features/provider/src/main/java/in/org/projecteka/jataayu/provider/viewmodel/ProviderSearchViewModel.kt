package `in`.org.projecteka.jataayu.provider.viewmodel

import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import android.util.Log
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

class ProviderSearchViewModel(val providerRepository: ProviderRepository) : ViewModel() {
    val providers = liveDataOf<List<ProviderInfo>>()

    fun getProvider(query: String) {
        providerRepository.getProvider(query).enqueue(object : retrofit2.Callback<List<ProviderInfo>?> {
            override fun onFailure(call: Call<List<ProviderInfo>?>, t: Throwable) {
                Log.e("Test", "Nothing found")
            }

            override fun onResponse(call: Call<List<ProviderInfo>?>, response: Response<List<ProviderInfo>?>) {
                if (response.body() != null) {
                    providers.value = response.body()
                }
            }
        })
    }
}
