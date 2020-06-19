package `in`.projecteka.jataayu.util

import `in`.projecteka.jataayu.util.extension.startActivityForResult
import android.content.Context
import android.content.Intent

typealias IntentDefinition = Intent.() -> Unit

private const val ACTIVITY_REGISTRATION = "in.projecteka.jataayu.registration.ui.activity.RegistrationActivity"
private const val ACTIVITY_PROVIDER = "in.projecteka.jataayu.provider.ui.ProviderActivity"
private const val ACTIVITY_ACCOUNT_CREATION = "in.projecteka.jataayu.user.account.ui.activity.AccountCreationActivity"
private const val ACTIVITY_LOGIN = "in.projecteka.jataayu.registration.ui.activity.LoginActivity"
private const val ACTIVITY_LAUNCHER = "in.projecteka.jataayu.ui.launcher.LauncherActivity"
private const val ACTIVITY_DASHBOARD = "in.projecteka.jataayu.ui.dashboard.DashboardActivity"
private const val ACTIVITY_FORGOT_PASSWORD = "in.projecteka.resetpassword.ui.activity.ResetPasswordActivity"
private const val ACTIVITY_PIN_VERIFICATION = "in.projecteka.jataayu.consent.ui.activity.PinVerificationActivity"
private const val ACTIVITY_CREATE_PIN = "in.projecteka.jataayu.consent.ui.activity.CreatePinActivity"
private const val ACTIVITY_CHANGE_PASSWORD = "in.projecteka.jataayu.user.account.ui.activity.ChangePasswordActivity"
private const val ACTIVITY_RECOVER_CMID = "in.projecteka.jataayu.user.account.ui.activity.RecoverCmidActivity"
private const val ACTIVITY_INTRO_SCREENS = "in.projecteka.jataayu.presentation.ui.activity.IntroScreensActivity"
private const val ACTIVITY_NO_INTERNET_CONNECTION_SCREEN = "in.projecteka.jataayu.presentation.ui.activity.NoInternetConnectionActivity"

private fun defaultIntentDefinition(
    context: Context,
    clazz: String,
    intentDefinition: IntentDefinition? = null
) = Intent(context, context.classLoader.loadClass(clazz))
    .apply {
        intentDefinition?.let { it(this) }
    }

fun startRegistration(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_REGISTRATION, intentDefinition))
}

fun startProvider(context: Context, resultCode: Int? = null, intentDefinition: IntentDefinition? = null) {
    resultCode?.let {
        context.startActivityForResult(context.classLoader.loadClass(ACTIVITY_REGISTRATION), it)
    } ?: run {
        context.startActivity(defaultIntentDefinition(context, ACTIVITY_PROVIDER, intentDefinition))
    }
}

fun startAccountCreation(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_ACCOUNT_CREATION, intentDefinition))
}

fun startLogin(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_LOGIN, intentDefinition))
}

fun startLauncher(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_LAUNCHER, intentDefinition))
}

fun startDashboard(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_DASHBOARD, intentDefinition))
}

fun startForgotPassword(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_FORGOT_PASSWORD,intentDefinition))
}

fun startChangePassword(context: Context) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_CHANGE_PASSWORD))
}

fun startPinVerification(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_PIN_VERIFICATION, intentDefinition))
}

fun startCreatePin(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_CREATE_PIN, intentDefinition))
}

fun startRecoverCmid(context: Context) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_RECOVER_CMID))
}

fun startIntroScreens(context: Context){
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_INTRO_SCREENS))
}

fun startNoInternetConnectionScreen(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_NO_INTERNET_CONNECTION_SCREEN, intentDefinition))
}

