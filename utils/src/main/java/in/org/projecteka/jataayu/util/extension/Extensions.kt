package `in`.org.projecteka.jataayu.util.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

fun <T> Context.startActivity(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

fun <T> Context.startActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun <T> Fragment.startActivity(clazz: Class<T>) {
    context?.startActivity(Intent(context, clazz))
}

fun String.toNameCase(): String =
    split(" ").map { it.toLowerCase(Locale.getDefault()) }.map { it.capitalize() }.asReversed().reduce { acc, s -> "$s $acc " }.trim()

fun String.matchesPartiallyWith(anotherString: String): Boolean {
    if (anotherString.contains(" ${this.trim()}", true)) return true
    return anotherString.split(" ").filter { it.length >= trim().length }.any { it.substring(0, trim().length) == this }
}

inline fun <reified T : View> Activity.findView(id: Int): T = findViewById(id)

inline fun <reified T : View> View.findView(id: Int): T = findViewById(id)

fun String.capitalize(): String {
    return if (isNotEmpty() && this[0].isLowerCase())
        substring(0, 1).toUpperCase(Locale.getDefault()) + substring(1) else this
}

fun View.getString(id: Int): String = context.getString(id)

fun Context.showLongToast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.showShortToast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Fragment.showLongToast(text: CharSequence) = context?.showLongToast(text)

fun Fragment.showShortToast(text: CharSequence) = context?.showShortToast(text)
