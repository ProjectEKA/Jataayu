package `in`.org.projecteka.jataayu.ui.activity

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.TESTS)
open class TestsOnlyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_layout)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}