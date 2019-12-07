package `in`.org.projecteka.jataayu.module

import `in`.org.projecteka.jataayu.repository.SampleRepository
import `in`.org.projecteka.jataayu.repository.SampleRepositoryImpl
import `in`.org.projecteka.jataayu.viewmodel.LauncherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LauncherViewModel(sampleRepository = get()) }
}

val repositoryModule = module {
    factory { SampleRepositoryImpl() as SampleRepository }
}