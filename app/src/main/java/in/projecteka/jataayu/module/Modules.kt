package `in`.projecteka.jataayu.module

import `in`.projecteka.jataayu.consent.remote.ConsentApis
import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.consent.repository.ConsentRepositoryImpl
import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.consent.repository.UserVerificationRepositoryImpl
import `in`.projecteka.jataayu.consent.viewmodel.*
import `in`.projecteka.jataayu.network.BuildConfig
import `in`.projecteka.jataayu.network.createNetworkClient
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.provider.remote.ProviderApis
import `in`.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.projecteka.jataayu.provider.repository.ProviderRepositoryImpl
import `in`.projecteka.jataayu.provider.viewmodel.ProviderActivityViewModel
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.registration.remote.AuthenticationApis
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepositoryImpl
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationFragmentViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationVerificationViewModel
import `in`.projecteka.jataayu.user.account.remote.UserAccountApis
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepositoryImpl
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import okhttp3.ResponseBody
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

val viewModelModule = module {
    viewModel { ProviderSearchViewModel(get()) }
    viewModel { RequestedListViewModel(get()) }
    viewModel { GrantedConsentListViewModel(get()) }
    viewModel { ConsentHostFragmentViewModel() }
    viewModel { UserAccountsViewModel(get()) }
    viewModel { UserVerificationViewModel(get()) }
    viewModel { EditConsentDetailsVM(get()) }
    viewModel { ConsentDetailsActivityViewModel() }
    viewModel { PinVerificationViewModel() }
    viewModel { GrantedConsentDetailsViewModel(get()) }
    viewModel { RequestedConsentDetailsViewModel(get()) }
    viewModel { ProviderActivityViewModel() }

    //Login
    viewModel { LoginViewModel(get()) }

    //Registrations
    viewModel { RegistrationActivityViewModel(get()) }
    viewModel { RegistrationFragmentViewModel() }
    viewModel { RegistrationVerificationViewModel() }
}

val repositoryModule = module {
    factory { ProviderRepositoryImpl(get()) as ProviderRepository }
    factory { ConsentRepositoryImpl(get()) as ConsentRepository }
    factory { UserAccountsRepositoryImpl(get()) as UserAccountsRepository }
    factory { AuthenticationRepositoryImpl(get()) as AuthenticationRepository }
    factory { UserVerificationRepositoryImpl(get()) as UserVerificationRepository }
}

val networkModule = module {
    single { createNetworkClient(get(), BuildConfig.DEBUG) }
    single<Converter<ResponseBody, ErrorResponse>> {
        get<Retrofit>().responseBodyConverter(
            ErrorResponse::class.java,
            arrayOfNulls<Annotation>(0)
        )
    }
    single { get<Retrofit>().create(ProviderApis::class.java) }
    single { get<Retrofit>().create(ConsentApis::class.java) }
    single { get<Retrofit>().create(UserAccountApis::class.java) }
    single { get<Retrofit>().create(AuthenticationApis::class.java) }
    single { get<Retrofit>().create(UserVerificationApis::class.java) }
}