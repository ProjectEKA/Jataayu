package `in`.projecteka.jataayu.module

import `in`.projecteka.jataayu.consent.viewmodel.*
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationFragmentViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationVerificationViewModel
import `in`.projecteka.jataayu.ui.LauncherViewModel
import `in`.projecteka.jataayu.ui.dashboard.DashboardViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.CreateAccountViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    //Launcher
    viewModel { LauncherViewModel(get(), get()) }
    //Dashboard
    viewModel { DashboardViewModel() }

    //Registrations
    viewModel { RegistrationActivityViewModel(get(), get(), get()) }
    viewModel { RegistrationFragmentViewModel() }
    viewModel { RegistrationVerificationViewModel() }

    //User Account
    viewModel { UserAccountsViewModel(get(), get(), get()) }

    //Create Account
    viewModel { CreateAccountViewModel(get(), get(), get()) }

    //Login
    viewModel { LoginViewModel(get(), get(), get()) }

    //Consent
    viewModel { ConsentHostFragmentViewModel() }
    viewModel { RequestedConsentViewModel(get(), get(), get()) }
    viewModel { GrantedConsentViewModel(get(), get()) }
    viewModel { ConsentViewModel(get()) }
    viewModel { EditConsentDetailsVM(get()) }

    //Provider Search
    viewModel { ProviderSearchViewModel(get(), get()) }

    //User Verification
    viewModel { UserVerificationViewModel(get(), get()) }

}