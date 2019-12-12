package `in`.org.projecteka.jataayu.util.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity

@RestrictTo(RestrictTo.Scope.TESTS)
open class TestsOnlyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}