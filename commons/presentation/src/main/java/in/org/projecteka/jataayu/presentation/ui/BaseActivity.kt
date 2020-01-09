package `in`.org.projecteka.jataayu.presentation.ui

import `in`.org.projecteka.jataayu.presentation.BuildConfig
import `in`.org.projecteka.jataayu.presentation.R
import `in`.org.projecteka.jataayu.presentation.databinding.NetworkPrefDialogBinding
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.util.constant.NetworkConstants.Companion.MOCKOON_URL
import `in`.org.projecteka.jataayu.util.constant.NetworkConstants.Companion.PROD_URL
import `in`.org.projecteka.jataayu.util.constant.NetworkConstants.Companion.TEST_URL
import `in`.org.projecteka.jataayu.util.sharedPref.NetworkSharedPrefsManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var networkPrefDialogBinding: NetworkPrefDialogBinding

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (BuildConfig.DEBUG) {
            if (KeyEvent.KEYCODE_VOLUME_UP == keyCode) {
                initNetworkDialog()
                return true
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun initNetworkDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        networkPrefDialogBinding = NetworkPrefDialogBinding.inflate(layoutInflater)

        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setTitle("Network settings")

        networkPrefDialogBinding.etEndpoint.setText(NetworkSharedPrefsManager.getBaseUrl(this))

        networkPrefDialogBinding.rgEnvironmentOptions.check(
            networkPrefDialogBinding.rgEnvironmentOptions.getChildAt(
                NetworkSharedPrefsManager.getEndpointIndex(
                    this
                )
            ).id
        )
        networkPrefDialogBinding.selectedEnvironmentIndex = NetworkSharedPrefsManager.getEndpointIndex(this)
        var radioButton: RadioButton =
            networkPrefDialogBinding.rgEnvironmentOptions.findViewById(networkPrefDialogBinding.rgEnvironmentOptions.checkedRadioButtonId)
        radioButton.isChecked = true

        networkPrefDialogBinding.rgEnvironmentOptions
            .setOnCheckedChangeListener { _, _ ->
                radioButton =
                    networkPrefDialogBinding.rgEnvironmentOptions.findViewById(networkPrefDialogBinding.rgEnvironmentOptions.checkedRadioButtonId)
                networkPrefDialogBinding.selectedEnvironmentIndex =
                    networkPrefDialogBinding.rgEnvironmentOptions.indexOfChild(radioButton)
                networkPrefDialogBinding.etEndpoint.setText(getUrlForSelectedEnvironment(networkPrefDialogBinding.selectedEnvironmentIndex!!))
                radioButton.isChecked = true
            }

        alertDialogBuilder.setCancelable(false).setPositiveButton(getString(android.R.string.ok)) { _, _ ->
            run {
                networkPrefDialogBinding.etEndpoint.text?.toString()?.let {
                    NetworkSharedPrefsManager.setNetworkPref(
                        context = this@BaseActivity,
                        environmentIndex = networkPrefDialogBinding.rgEnvironmentOptions.indexOfChild(radioButton),
                        endpoint = it
                    )
                }
            }
        }.setNegativeButton(getString(android.R.string.cancel)) { dialog, _ -> dialog.cancel() }
        alertDialogBuilder.setView(networkPrefDialogBinding.root)
        alertDialogBuilder.show()
    }

    private fun getUrlForSelectedEnvironment(selectedEnvironmentIndex: Int): String {
        return when (selectedEnvironmentIndex) {
            0 -> PROD_URL
            1 -> TEST_URL
            else -> MOCKOON_URL
        }
    }

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

    protected open fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack(fragment.javaClass.name).commit()
    }

    protected open fun addFragment(fragment: Fragment) {
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