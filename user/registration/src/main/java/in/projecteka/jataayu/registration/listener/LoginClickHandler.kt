package `in`.projecteka.jataayu.registration.listener

import android.view.View

interface LoginClickHandler{
    fun onRegisterClick(view: View)
    fun onLoginClick(view: View)
    fun showOrHidePassword(view: View)
}