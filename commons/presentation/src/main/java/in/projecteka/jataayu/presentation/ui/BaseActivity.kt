package `in`.projecteka.jataayu.presentation.ui

import `in`.projecteka.jataayu.presentation.BuildConfig
import `in`.projecteka.jataayu.presentation.R
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.databinding.NetworkPrefDialogBinding
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.MOCKOON_URL
import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.PROD_URL
import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.TEST_URL
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.sharedPref.getBaseUrl
import `in`.projecteka.jataayu.util.sharedPref.getEndpointIndex
import `in`.projecteka.jataayu.util.sharedPref.setNetworkPref
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.base_activity.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.getKoin


abstract class BaseActivity : AppCompatActivity() {
    private lateinit var binding: BaseActivityBinding
    private lateinit var networkPrefDialogBinding: NetworkPrefDialogBinding
    protected val eventBusInstance = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.base_activity)
        setSupportActionBar(toolbar)
        supportFragmentManager.apply {
            addOnBackStackChangedListener {
                if (fragments.isNotEmpty()) fragments.last {
                    (it as? BaseFragment)?.onVisible()
                    return@last true
                }
            }
        }
    }

    private val okListener = DialogInterface.OnClickListener { _, _ ->
        networkPrefDialogBinding.apply {
            etEndpoint.text?.toString()?.let {
                setNetworkPref(selectedEnvironmentIndex!!, it)
            }
            etAuthToken.text?.toString()?.let {
                getKoin().get<CredentialsRepository>().accessToken = etAuthToken.text.toString()
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
            endpoint = getBaseUrl()
            token = getKoin().get<CredentialsRepository>().accessToken
            rgEnvironmentOptions.check(rgEnvironmentOptions.getChildAt(getEndpointIndex()).id)
            selectedEnvironmentIndex = getEndpointIndex()

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
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(fragment.javaClass.name).commit()
    }

    open fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(fragment.javaClass.name)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!isProgressBarShowing()) {
            super.onBackPressed()
            if (supportFragmentManager.backStackEntryCount == 0) finish()
        }
    }

    open fun setProgressBarVisibilityValue(shouldShow: Boolean) {
        binding.progressBarVisibility = shouldShow
    }

    open fun setProgressBarMessage(message: String) {
        binding.progressBarMessage = message
    }

    open fun showProgressBar(shouldShow: Boolean) {
        showProgressBar(shouldShow, "")
    }

    open fun showProgressBar(shouldShow: Boolean, message: String) {
        setProgressBarVisibilityValue(shouldShow)
        setProgressBarMessage(message)
    }

    private fun isProgressBarShowing(): Boolean {
        return binding.progressBarVisibility == true
    }
}