package `in`.org.projecteka.jataayu.module

import `in`.org.projecteka.jataayu.BuildConfig
import `in`.org.projecteka.jataayu.datasource.api.SampleApi
import `in`.org.projecteka.jataayu.network.createNetworkClient
import `in`.org.projecteka.jataayu.repository.SampleRepository
import `in`.org.projecteka.jataayu.repository.SampleRepositoryImpl
import `in`.org.projecteka.jataayu.viewmodel.LauncherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val viewModelModule = module {
    viewModel { LauncherViewModel(sampleRepository = get()) }
}

val repositoryModule = module {
    factory { SampleRepositoryImpl(sampleApi = get()) as SampleRepository }
}

val networkModule = module {
    single { sampleApi }
}

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

private val retrofit: Retrofit = createNetworkClient(BASE_URL, BuildConfig.DEBUG)

private val sampleApi: SampleApi = retrofit.create(SampleApi::class.java)