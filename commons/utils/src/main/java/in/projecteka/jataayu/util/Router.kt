package `in`.projecteka.jataayu.util

import android.content.Context
import android.content.Intent

typealias IntentDefinition = Intent.() -> Unit

private const val ACTIVITY_REGISTRATION = "in.projecteka.jataayu.registration.ui.activity.RegistrationActivity"
private const val ACTIVITY_PROVIDER = "in.projecteka.jataayu.provider.ui.ProviderActivity"
private const val ACTIVITY_ACCOUNT_CREATION = "in.projecteka.jataayu.user.account.ui.activity.AccountCreationActivity"
private const val ACTIVITY_LOGIN = "in.projecteka.jataayu.registration.ui.activity.LoginActivity"
private const val ACTIVITY_LAUNCHER = "in.projecteka.jataayu.ui.LauncherActivity"
private const val ACTIVITY_DASHBOARD = "in.projecteka.jataayu.ui.dashboard.DashboardActivity"

private fun defaultIntentDefinition(
    context: Context,
    clazz: String,
    intentDefinition: IntentDefinition? = null
) = Intent(context, context.classLoader.loadClass(clazz))
    .apply {
        intentDefinition?.let { it(this) }
    }

fun startRegistration(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(Intent(context, context.classLoader.loadClass(ACTIVITY_REGISTRATION)).apply {
        intentDefinition?.let { it(this) }
    })
}

fun startProvider(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(defaultIntentDefinition(context, ACTIVITY_PROVIDER, intentDefinition))
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