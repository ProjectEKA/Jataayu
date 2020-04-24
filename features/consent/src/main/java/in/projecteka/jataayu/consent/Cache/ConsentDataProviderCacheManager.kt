package `in`.projecteka.jataayu.consent.Cache

import `in`.projecteka.jataayu.consent.Cache.ConsentCache.providerMap
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Hip
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.network.utils.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer


class ConsentDataProviderCacheManager {

    private var hipInfoTasksCount = 0
    private lateinit var completion: () -> Unit


    private fun getHipInfoLiveData(
        providerId: String,
        repository: ConsentRepository
    ): PayloadLiveData<ProviderInfo> {
        val liveData = PayloadLiveData<ProviderInfo>()
        liveData.fetch(repository.getProviderBy(providerId))
        return liveData
    }

    /*
    *  providerIdList - list of HIP or HIU ids
    *  pass the ConsentRepository and lifecycle object and update the response in cache
    *  call getProviderBy method to get the individual value of corresponding ids.
    * */

    fun fetchHipInfo(
        providerIdList: List<String>,
        repository: ConsentRepository,
        lifecycleOwner: LifecycleOwner,
        completion: () -> Unit
    ) {
        this.completion = completion
        for (providerId in providerIdList.toSet()) {
            hipInfoTasksCount += 1
            if (providerMap[providerId] == null) {
                val liveData =
                    getHipInfoLiveData(
                        providerId,
                        repository
                    )
                liveData.observe(
                    lifecycleOwner,
                    Observer<PayloadResource<ProviderInfo>> { payload ->
                        when (payload) {
                            is Success -> {
                                hipInfoTasksCount -= 1
                                payload.data?.hip?.let {
                                    providerMap[it.id] = payload.data!!
                                }
                            }
                            is Loading -> {
                            }
                            else -> {
                                decrementHipInfoTasksCount()
                            }
                        }
                        finishTasksIfNeeded(
                            lifecycleOwner
                        )
                    })
            } else {
                decrementHipInfoTasksCount()
                finishTasksIfNeeded(
                    lifecycleOwner
                )
            }
        }
    }

    private fun decrementHipInfoTasksCount() {
        hipInfoTasksCount -= 1
    }

    private fun finishTasksIfNeeded(lifecycleOwner: LifecycleOwner) {
        if (hipInfoTasksCount == 0) {
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)) {
                completion()
            }
        }
    }

    /*Returns HI Provider or HI User info
    * for given HIP or HIU ID
    * */
    fun getProviderBy(id: String): Hip? {
        return providerMap[id]?.hip
    }
}

private object ConsentCache {
    var providerMap = HashMap<String, ProviderInfo>()
        private set
}