package `in`.projecteka.jataayu.util.extension

import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

fun <T> ViewModel.liveDataOf() = MutableLiveData<T>()

fun <T> Context.startActivity(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

fun <T> Fragment.startActivity(clazz: Class<T>) {
    context?.startActivity(Intent(context, clazz))
}

fun <T> Context.startActivityForResult(clazz: Class<T>, requestCode: Int) {
    (this as Activity).startActivityForResult(Intent(this, clazz), requestCode)
}

fun <T> Fragment.startActivityForResult(clazz: Class<T>, requestCode: Int) {
    activity!!.startActivityForResult(Intent(activity, clazz), requestCode)
}

fun Fragment.setTitle(@StringRes resourceId: Int) {
    activity?.setTitle(resourceId)
}

inline fun <reified T : View> Activity.findView(id: Int): T = findViewById(id)

inline fun <reified T : View> View.findView(id: Int): T = findViewById(id)

fun View.getString(id: Int): String = context.getString(id)

val String.Companion.EMPTY: String get() = ""

fun String.mask(): String? {
    return if (this.length > 4) {
        this.substring(0, 2) + this.substring(2, length - 2).replace(
            Regex("[0-9]"),
            "X"
        ) + this.substring(length - 2)
    } else this
}

fun Context.showLongToast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
fun Fragment.showLongToast(text: CharSequence) = context?.showLongToast(text)
fun Context.showShortToast(text: CharSequence) =
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

val Context.isConnected: Boolean
    get() {
        val connectivityManager =
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    true
                }
                else -> false
            }
        } else {
            false
        }
    }

fun Fragment.showShortToast(text: CharSequence) = context?.showShortToast(text)

fun Date.toUtc(): String {
    return DateTimeUtils.getUtcDate(this)
}

inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)

fun View.show(shouldShow: Boolean) {
    this.visibility = if (shouldShow) View.VISIBLE else View.GONE
}

fun <T> Observable<T>.get(): Observable<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.getIo(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun showSnackbar(
    view: View, message: String,
    duration: Int = 2000,
    callback: Snackbar.Callback? = null
) {
    val spannableString = SpannableString(message)
    spannableString.setSpan(
        ForegroundColorSpan(Color.WHITE),
        0,
        message.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    val snackbar = Snackbar.make(view, spannableString, duration)
    callback?.let { snackbar.addCallback(it) }
    snackbar.show()
}