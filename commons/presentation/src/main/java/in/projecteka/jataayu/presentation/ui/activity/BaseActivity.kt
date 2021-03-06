package `in`.projecteka.jataayu.presentation.ui.activity

import `in`.projecteka.jataayu.presentation.BuildConfig
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
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.getKoin


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private lateinit var networkPrefDialogBinding: NetworkPrefDialogBinding
    protected val eventBusInstance = EventBus.getDefault()
    lateinit var binding: T

    @LayoutRes
    abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        addBackStackWatcher()
    }

    private fun addBackStackWatcher() {
        supportFragmentManager.apply {
            addOnBackStackChangedListener {
                if (fragments.isNotEmpty()) fragments.last {
                    (it as? BaseFragment)?.onVisible()
                    return@last true
                }
            }
        }
    }

    private val networkDialogOkListener = DialogInterface.OnClickListener { _, _ ->
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

        networkPrefDialogBinding.run {
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
                .setCancelable(false).setPositiveButton(getString(android.R.string.ok), networkDialogOkListener)
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

    open fun replaceFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)

        if(addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.name)
        }

        transaction.commit()
    }

    open fun addFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .add(containerId, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.name)
        }

        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
//        if (!isProgressBarShowing()) {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) finish()
//        }
    }

//    fun showProgressBar(shouldShow: Boolean, @StringRes messageId: Int) {
//        baseViewModel()?.showProgress?.set(shouldShow)
//        baseViewModel()?.showProgressMessage?.set(messageId)
//    }
//
//    fun showProgressBar(shouldShow: Boolean, messageId: String) {
//        baseViewModel()?.showProgress?.set(shouldShow)
////        baseViewModel()?.showProgressMessage?.set(messageId)
//    }
//    fun showProgressBar(shouldShow: Boolean) {
//        baseViewModel()?.showProgress?.set(shouldShow)
////        baseViewModel()?.showProgressMessage?.set(messageId)
//    }
//
//    private fun isProgressBarShowing(): Boolean = baseViewModel()?.showProgress?.get() ?: false
}