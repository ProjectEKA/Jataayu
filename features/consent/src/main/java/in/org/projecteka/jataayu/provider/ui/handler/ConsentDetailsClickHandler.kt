package `in`.org.projecteka.jataayu.provider.ui.handler

import android.view.View

interface ConsentDetailsClickHandler {
    fun onEditClick(view: View)
    fun onGrantConsent(view: View)
    fun onDenyConsent(view: View)
}