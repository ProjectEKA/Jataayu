package `in`.org.projecteka.jataayu.module

import `in`.org.projecteka.featuresprovider.BuildConfig
import `in`.org.projecteka.jataayu.consent.remote.ConsentApis
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepositoryImpl
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.network.createNetworkClient
import `in`.org.projecteka.jataayu.provider.remote.ProviderApis
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepositoryImpl
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.org.projecteka.jataayu.registration.remote.AuthorizationApis
import `in`.org.projecteka.jataayu.registration.repository.AuthorizationRepository
import `in`.org.projecteka.jataayu.registration.repository.AuthorizationRepositoryImpl
import `in`.org.projecteka.jataayu.registration.viewmodel.RegistrationViewModel
import `in`.org.projecteka.jataayu.user.account.remote.UserAccountApis
import `in`.org.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.org.projecteka.jataayu.user.account.repository.UserAccountsRepositoryImpl
import `in`.org.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProviderSearchViewModel(get()) }
    viewModel { ConsentViewModel(get()) }
    viewModel { UserAccountsViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
}

val repositoryModule = module {
    factory { ProviderRepositoryImpl(get()) as ProviderRepository }
    factory { ConsentRepositoryImpl(get()) as ConsentRepository }
    factory { UserAccountsRepositoryImpl(get()) as UserAccountsRepository }
    factory { AuthorizationRepositoryImpl(get()) as AuthorizationRepository }
}

val networkModule = module {
    single { createNetworkClient(get(), BuildConfig.DEBUG).create(ProviderApis::class.java) }
    single { createNetworkClient(get(), BuildConfig.DEBUG).create(ConsentApis::class.java) }
    single { createNetworkClient(get(), BuildConfig.DEBUG).create(UserAccountApis::class.java) }
    single { createNetworkClient(get(), BuildConfig.DEBUG).create(AuthorizationApis::class.java) }
}