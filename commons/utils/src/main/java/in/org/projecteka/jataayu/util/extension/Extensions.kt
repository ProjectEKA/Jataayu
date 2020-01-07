package `in`.org.projecteka.jataayu.util.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun <T> Context.startActivity(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

fun <T> Fragment.startActivity(clazz: Class<T>) {
    context?.startActivity(Intent(context, clazz))
}

fun Fragment.setTitle(@StringRes resourceId: Int) {
    activity?.setTitle(resourceId)
}


inline fun <reified T : View> Activity.findView(id: Int): T = findViewById(id)

inline fun <reified T : View> View.findView(id: Int): T = findViewById(id)

fun View.getString(id: Int): String = context.getString(id)

val String.Companion.EMPTY: String get() = ""

public fun String.mask(): String? {
    if (this.length > 4) {
        return this.substring(0, 2) + this.substring(2, length - 2).replace(
            Regex("[0-9]"),
            "X"
        ) + this.substring(length - 2)
    } else return this
}

fun Context.showLongToast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
fun Fragment.showLongToast(text: CharSequence) = context?.showLongToast(text)
fun Context.showShortToast(text: CharSequence) =
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Fragment.showShortToast(text: CharSequence) = context?.showShortToast(text)
