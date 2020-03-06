package `in`.projecteka.jataayu.presentation

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun View.wobble() {
    val loadAnimation = AnimationUtils.loadAnimation(context, R.anim.wobble)
    this.animation = loadAnimation
    loadAnimation.startNow()
}

fun Context.showErrorDialog(errorMessage: String?) {
    showAlertDialog(
        getString(R.string.failure), errorMessage ?: getString(R.string.something_went_wrong), getString(android.R.string.ok)
    )
}

fun Context.showAlertDialog(
    title: String,
    message: String?,
    positiveBtnText: String,
    positiveBtnClickListener: DialogInterface.OnClickListener? = null,
    negativeBtnText: String? = null,
    negativeBtnClickListener: DialogInterface.OnClickListener? = null
) {

    val defaultClickListener = DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() }

    var builder = MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setPositiveButton(
            positiveBtnText,
            positiveBtnClickListener ?: defaultClickListener)

    message?.let { builder = builder.setMessage(message) }
    negativeBtnText?.let { builder = builder.setNegativeButton(negativeBtnText, negativeBtnClickListener ?: defaultClickListener) }

    builder.show()
}

fun Context.showAlertDialog(
    @StringRes titleResId: Int,
    @StringRes messageResId: Int?,
    @StringRes positiveBtnResId: Int,
    positiveBtnClickListener: DialogInterface.OnClickListener? = null,
    @StringRes negativeBtnResId: Int? = null,
    negativeBtnClickListener: DialogInterface.OnClickListener? = null
) {
    return when (messageResId) {
        null -> when (negativeBtnResId) {
            null -> showAlertDialog(
                getString(titleResId), null, getString(positiveBtnResId),
                positiveBtnClickListener, null, negativeBtnClickListener
            )
            else -> showAlertDialog(
                getString(titleResId), null, getString(positiveBtnResId),
                positiveBtnClickListener, getString(negativeBtnResId), negativeBtnClickListener
            )
        }
        else -> when (negativeBtnResId) {
            null -> showAlertDialog(
                getString(titleResId), getString(messageResId), getString(positiveBtnResId),
                positiveBtnClickListener, null, negativeBtnClickListener
            )
            else -> showAlertDialog(
                getString(titleResId), getString(messageResId), getString(positiveBtnResId),
                positiveBtnClickListener, getString(negativeBtnResId), negativeBtnClickListener
            )
        }
    }
}