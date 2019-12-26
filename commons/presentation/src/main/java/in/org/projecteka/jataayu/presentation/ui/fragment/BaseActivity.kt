package `in`.org.projecteka.jataayu.presentation.ui.fragment

import `in`.org.projecteka.jataayu.presentation.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.provider_search_activity)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.fragments.isNotEmpty()) {
                supportFragmentManager.fragments.last {
                    (it as BaseFragment).onVisible()
                    return@last true
                }
            }
        }
    }

    protected fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack(fragment.javaClass.name).commit()
    }

    protected fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment)
            .addToBackStack(fragment.javaClass.name).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) finish()
    }
}