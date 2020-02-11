package `in`.projecteka.jataayu.module

import `in`.projecteka.featuresprovider.BuildConfig
import `in`.projecteka.jataayu.consent.remote.ConsentApis
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.consent.repository.ConsentRepositoryImpl
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.network.createNetworkClient
import `in`.projecteka.jataayu.provider.remote.ProviderApis
import `in`.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.projecteka.jataayu.provider.repository.ProviderRepositoryImpl
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.registration.remote.AuthorizationApis
import `in`.projecteka.jataayu.registration.repository.AuthorizationRepository
import `in`.projecteka.jataayu.registration.repository.AuthorizationRepositoryImpl
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationViewModel
import `in`.projecteka.jataayu.user.account.remote.UserAccountApis
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepositoryImpl
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
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