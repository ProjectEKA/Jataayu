package `in`.org.projecteka.jataayu.ui

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.consent.ui.activity.ConsentActivity
import `in`.org.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.org.projecteka.jataayu.provider.ui.ProviderSearchActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLauncherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLauncherBinding>(
            this,
            R.layout.activity_launcher
        )

        initBindings()
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivity(Intent(this, ProviderSearchActivity::class.java))
        }
    }

    private fun initBindings() {
        binding.bottomNavListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
                if (menuItem.itemId == R.id.action_consents) {
                    showConsentScreen()
                }
                true
            }
    }

    private fun showConsentScreen() {
        startActivity(Intent(this, ConsentActivity::class.java))
    }
}
