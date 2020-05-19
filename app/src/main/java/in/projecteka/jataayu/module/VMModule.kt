package `in`.projecteka.jataayu.module

import `in`.projecteka.jataayu.consent.viewmodel.*
import `in`.projecteka.jataayu.provider.viewmodel.ProviderActivityViewModel
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.registration.viewmodel.*
import `in`.projecteka.jataayu.ui.LauncherViewModel
import `in`.projecteka.jataayu.ui.dashboard.DashboardViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.ChangePasswordViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.CreateAccountViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.ProfileActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.ProfileFragmentViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import `in`.projecteka.resetpassword.viewmodel.ReadIdentifierFragmentViewModel
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordActivityViewModel
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordFragmentViewModel
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordOtpVerificationViewModel
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
    viewModel { LoginViewModel() }
    viewModel { ConsentManagerIDInputViewModel(get()) }
    viewModel { PasswordInputViewModel(get(), get(), get()) }
    viewModel { LoginOtpViewModel(get(), get(), get()) }

    //Consent
    viewModel { ConsentHostFragmentViewModel() }
    viewModel { EditConsentDetailsVM(get()) }
    viewModel { RequestedListViewModel(get()) }
    viewModel { GrantedConsentListViewModel(get(), get()) }
    viewModel { RequestedConsentDetailsViewModel(get(), get(), get()) }
    viewModel { GrantedConsentDetailsViewModel(get()) }

    //Provider Search
    viewModel { ProviderSearchViewModel(get(), get(), get()) }
    viewModel { ProviderActivityViewModel() }

    //User Verification
    viewModel { UserVerificationViewModel(get(), get(), get()) }
    viewModel { PinVerificationViewModel() }

    //Forgot Password
    viewModel { ResetPasswordActivityViewModel() }
    viewModel { ReadIdentifierFragmentViewModel(get()) }
    viewModel { ResetPasswordOtpVerificationViewModel(get()) }
    viewModel { ResetPasswordFragmentViewModel(get()) }

    //Change Password
    viewModel { ChangePasswordViewModel(get()) }

    //Profile
    viewModel { ProfileActivityViewModel()}
    viewModel { ProfileFragmentViewModel(get(), get(), get()) }

}