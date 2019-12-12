package `in`.org.projecteka.jataayu

import `in`.org.projecteka.jataayu.module.networkModule
import `in`.org.projecteka.jataayu.module.repositoryModule
import `in`.org.projecteka.jataayu.module.viewModelModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

open class JataayuApp : Application() {
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
            loadKoinModules(listOf(viewModelModule, repositoryModule, networkModule))
        }
    }
}