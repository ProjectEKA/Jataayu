package `in`.projecteka.jataayu.util

import android.content.Context
import android.content.Intent

typealias IntentDefinition = Intent.() -> Unit

private const val ACTIVITY_REGISTRATION = "in.projecteka.jataayu.registration.ui.activity.RegistrationActivity"
private const val ACTIVITY_LOGIN = "in.projecteka.jataayu.registration.ui.activity.LoginActivity"

fun startRegistration(context: Context, intentDefinition: IntentDefinition? = null) {
    context.startActivity(Intent(context, context.classLoader.loadClass(ACTIVITY_REGISTRATION)).apply {
        intentDefinition?.let { it(this) }
    })
}

