package `in`.projecteka.jataayu.presentation.ui.activity

import `in`.projecteka.jataayu.presentation.R
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class NoInternetConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet_connection)
    }

     fun onRetryClick(view: View) {
        finish()
    }
}
