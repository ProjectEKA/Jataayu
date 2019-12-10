package `in`.org.projecteka.jataayu.provider.ui

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ProviderSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.provider_search_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProviderSearchFragment.newInstance())
                .commitNow()
        }
    }

}
