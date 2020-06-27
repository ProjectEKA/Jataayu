package `in`.projecteka.jataayu.module

import `in`.projecteka.jataayu.BuildConfig
import `in`.projecteka.jataayu.consent.remote.ConsentApis
import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.consent.repository.ConsentRepositoryImpl
import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.consent.repository.UserVerificationRepositoryImpl
import `in`.projecteka.jataayu.core.remote.UserAccountApis
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.core.repository.UserAccountsRepositoryImpl
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.NetworkManager
import `in`.projecteka.jataayu.provider.remote.ProviderApis
import `in`.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.projecteka.jataayu.provider.repository.ProviderRepositoryImpl
import `in`.projecteka.jataayu.registration.remote.AuthenticationApis
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepositoryImpl
import `in`.projecteka.jataayu.user.account.remote.ChangePasswordApis
import `in`.projecteka.jataayu.user.account.repository.ChangePasswordRepository
import `in`.projecteka.jataayu.user.account.repository.ChangePasswordRepositoryImpl
import `in`.projecteka.jataayu.util.repository.*
import `in`.projecteka.resetpassword.remote.ResetPasswordApis
import `in`.projecteka.resetpassword.repository.ResetPasswordRepository
import `in`.projecteka.resetpassword.repository.ResetPasswordRepositoryImpl
import okhttp3.ResponseBody
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

val repositoryModule = module {
    factory { ProviderRepositoryImpl(get()) as ProviderRepository }
    factory { ConsentRepositoryImpl(get()) as ConsentRepository }
    factory { UserAccountsRepositoryImpl(
        get()
    ) as UserAccountsRepository
    }
    factory { AuthenticationRepositoryImpl(get()) as AuthenticationRepository }
    factory { UserVerificationRepositoryImpl(get()) as UserVerificationRepository }
    factory { UUIDRepositoryImpl() as UUIDRepository }
    factory { ResetPasswordRepositoryImpl(get()) as ResetPasswordRepository }
    factory { ChangePasswordRepositoryImpl(get()) as ChangePasswordRepository }

    single { PreferenceRepositoryImpl(get(named(ENCRYPTED_PREFS))) as PreferenceRepository }
    single { CredentialsRepositoryImpl(get(named(ENCRYPTED_PREFS))) as CredentialsRepository }
}

val networkModule = module {
    single { NetworkManager(get()) }
    single { get<NetworkManager>().createNetworkClient(get(), get(),BuildConfig.DEBUG) }
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
    single { get<Retrofit>().create(ResetPasswordApis::class.java) }
    single { get<Retrofit>().create(UserVerificationApis::class.java) }
    single { get<Retrofit>().create(ChangePasswordApis::class.java) }
}