package `in`.projecteka.jataayu

import `in`.projecteka.jataayu.module.networkModule
import `in`.projecteka.jataayu.module.preferenceModule
import `in`.projecteka.jataayu.module.repositoryModule
import `in`.projecteka.jataayu.module.viewModelModule
import `in`.projecteka.jataayu.network.utils.NetworkStatus
import `in`.projecteka.jataayu.network.utils.NoInternetMessage
import `in`.projecteka.jataayu.presentation.ui.activity.NoInternetConnectionActivity
import `in`.projecteka.jataayu.util.livedata.NetworkConnectionLiveData
import android.app.Application
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

open class JataayuApp : Application() {

    private var isNoNetworkScreenShown = false
    override fun onCreate() {
        super.onCreate()
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return super.createStackElementTag(element) + ":" + element.lineNumber
            }
        })
        startKoin {
            androidLogger()
            androidContext(this@JataayuApp)
            loadKoinModules(listOf(preferenceModule, networkModule, repositoryModule,viewModelModule))
        }
        NetworkConnectionLiveData(this).observeForever{
            EventBus.getDefault().post(NetworkStatus(it))
        }
        registerEvent()
    }

    private fun registerEvent() {
        if (!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this)
    }

    // this method will be called by event bus
    @Subscribe
    fun onNoInternetEventReceived(noInternetMessage: NoInternetMessage) {
        if (!isNoNetworkScreenShown) {
            NoInternetConnectionActivity.start(this) {
                noInternetMessage.retryCallBack.invoke()
                isNoNetworkScreenShown = false
            }
            isNoNetworkScreenShown = true
        }
    }
}