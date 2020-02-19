package `in`.projecteka.jataayu.user.account.listener

import android.view.View

interface AccountCreationClickHandler {
    fun showOrHidePassword(view: View)
    fun createAccount(view: View)
    fun onSelectDateClick(view: View)
}