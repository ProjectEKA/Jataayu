package `in`.projecteka.jataayu.module

import `in`.projecteka.jataayu.consent.viewmodel.*
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationFragmentViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationVerificationViewModel
import `in`.projecteka.jataayu.ui.dashboard.DashboardViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.CreateAccountViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProviderSearchViewModel(get()) }
    viewModel { RequestedConsentViewModel(get()) }
    viewModel { GrantedConsentViewModel(get()) }
    viewModel { ConsentViewModel(get()) }

    viewModel { UserVerificationViewModel(get()) }
    viewModel { EditConsentDetailsVM() }

    //Dashboard
    viewModel { DashboardViewModel() }

    //Login
    viewModel { LoginViewModel(get()) }

    //Registrations
    viewModel { RegistrationActivityViewModel(get()) }
    viewModel { RegistrationFragmentViewModel() }
    viewModel { RegistrationVerificationViewModel() }

    //User Account
    viewModel { UserAccountsViewModel(get()) }

    //Create Account
    viewModel { CreateAccountViewModel(get()) }
}