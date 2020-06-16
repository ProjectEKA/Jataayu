package `in`.projecteka.jataayu.presentation.ui.activity

import `in`.projecteka.jataayu.presentation.R
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.util.startNoInternetConnectionScreen
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class NoInternetConnectionActivity : BaseActivity<BaseActivityBinding>() {

    companion object {
        private var completion: (() -> Unit)? = null
        fun start(context: Context, completion: (() -> Unit)?) {
            NoInternetConnectionActivity.completion = completion
            startNoInternetConnectionScreen(context) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
    }

    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // need to disable retry button until network connected.
        retryButton = findViewById(R.id.btn_retry)
    }

    fun onRetryClick(view: View) {
        finish()
        completion?.invoke()
    }

    override fun layoutId(): Int {
        return R.layout.activity_no_internet_connection
    }
}
