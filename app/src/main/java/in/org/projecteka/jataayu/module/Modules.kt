package `in`.org.projecteka.jataayu.module

import `in`.org.projecteka.featuresprovider.BuildConfig
import `in`.org.projecteka.jataayu.network.createNetworkClient
import `in`.org.projecteka.jataayu.provider.remote.ProviderApis
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepositoryImpl
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ProviderSearchViewModel(
            providerRepository = get())
    }
}

val repositoryModule = module {
    factory { ProviderRepositoryImpl(providerApi = get()) as ProviderRepository }
}

val networkModule = module {
    single { createNetworkClient(get(), BuildConfig.DEBUG).create(ProviderApis::class.java) }
}
