package `in`.projecteka.jataayu.ui.activity

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.TESTS)
open class TestsOnlyActivity : BaseActivity<BaseActivityBinding>() {

    override fun layoutId(): Int = R.layout.base_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}