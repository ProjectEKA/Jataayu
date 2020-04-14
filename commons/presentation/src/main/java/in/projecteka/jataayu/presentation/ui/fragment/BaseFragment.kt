package `in`.projecteka.jataayu.presentation.ui.fragment

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    open fun onVisible() {}
    open fun onBackPressedCallback() {}

    protected fun showProgressBar(shouldShow: Boolean) {
        if (activity?.isFinishing == false) {
            showProgressBar(shouldShow, "")
        }
    }

    protected fun showProgressBar(shouldShow: Boolean, progressBarMessage: String) {
        activity?.let {
            if (it is BaseActivity) {
                it.showProgressBar(shouldShow, progressBarMessage)
            }
        }
    }
}