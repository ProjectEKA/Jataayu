package `in`.projecteka.jataayu.presentation.ui.fragment

import `in`.projecteka.jataayu.presentation.R
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment : DialogFragment() {
    open fun onVisible() {}
    open fun onBackPressedCallback() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    protected fun showProgressBar(shouldShow: Boolean) {
        showProgressBar(shouldShow, "")
    }

    protected fun showProgressBar(shouldShow: Boolean, progressBarMessage: String) {
        activity?.let{
            if (it is BaseActivity) {
                it.showProgressBar(shouldShow, progressBarMessage)
            }
        }
    }
}