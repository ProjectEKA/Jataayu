package `in`.org.projecteka.jataayu.presentation.ui

import `in`.org.projecteka.jataayu.presentation.BuildConfig
import `in`.org.projecteka.jataayu.presentation.R
import `in`.org.projecteka.jataayu.presentation.databinding.NetworkPrefDialogBinding
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.util.constant.NetworkConstants.Companion.MOCKOON_URL
import `in`.org.projecteka.jataayu.util.constant.NetworkConstants.Companion.PROD_URL
import `in`.org.projecteka.jataayu.util.constant.NetworkConstants.Companion.TEST_URL
import `in`.org.projecteka.jataayu.util.sharedPref.NetworkSharedPrefsManager.Companion.getAuthToken
import `in`.org.projecteka.jataayu.util.sharedPref.NetworkSharedPrefsManager.Companion.getBaseUrl
import `in`.org.projecteka.jataayu.util.sharedPref.NetworkSharedPrefsManager.Companion.getEndpointIndex
import `in`.org.projecteka.jataayu.util.sharedPref.NetworkSharedPrefsManager.Companion.setAuthToken
import `in`.org.projecteka.jataayu.util.sharedPref.NetworkSharedPrefsManager.Companion.setNetworkPref
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var networkPrefDialogBinding: NetworkPrefDialogBinding

    private val okListener = DialogInterface.OnClickListener { _, _ ->
        networkPrefDialogBinding.apply {
            etEndpoint.text?.toString()?.let {
                setNetworkPref(this@BaseActivity, selectedEnvironmentIndex!!, it)
            }
            etAuthToken.text?.toString()?.let {
                setAuthToken(context = this@BaseActivity, authToken = etAuthToken.text.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        supportFragmentManager.apply {
            addOnBackStackChangedListener {
                if (fragments.isNotEmpty()) fragments.last {
                    (it as BaseFragment).onVisible()
                    return@last true
                }
            }
        }
    }

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

        networkPrefDialogBinding.apply {
            endpoint = getBaseUrl(this@BaseActivity)
            authToken = getAuthToken(this@BaseActivity)
            rgEnvironmentOptions.check(rgEnvironmentOptions.getChildAt(getEndpointIndex(this@BaseActivity)).id)
            selectedEnvironmentIndex = getEndpointIndex(this@BaseActivity)

            rgEnvironmentOptions.setOnCheckedChangeListener { _, _ ->
                selectedEnvironmentIndex = rgEnvironmentOptions.indexOfChild(
                    rgEnvironmentOptions.findViewById<RadioButton>(rgEnvironmentOptions.checkedRadioButtonId)
                )
                etEndpoint.setText(getUrlForSelectedEnvironment(selectedEnvironmentIndex!!))
            }
        }

        alertDialogBuilder.apply {
            setCancelable(false).setTitle("Network settings")
                .setCancelable(false).setPositiveButton(getString(android.R.string.ok), okListener)
                .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ -> dialog.cancel() }
            setView(networkPrefDialogBinding.root)
            show()
        }
    }

    private fun getUrlForSelectedEnvironment(selectedEnvironmentIndex: Int): String {
        return when (selectedEnvironmentIndex) {
            0 -> PROD_URL
            1 -> TEST_URL
            else -> MOCKOON_URL
        }
    }

    open fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack(fragment.javaClass.name).commit()
    }

    open fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack(fragment.javaClass.name)
            .commit()

        Log.d("TAG", "Backstack ${supportFragmentManager.backStackEntryCount}")
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