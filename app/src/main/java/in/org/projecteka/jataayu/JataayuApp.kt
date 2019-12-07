package `in`.org.projecteka.jataayu

import `in`.org.projecteka.jataayu.module.repositoryModule
import `in`.org.projecteka.jataayu.module.viewModelModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class JataayuApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@JataayuApp)
            loadKoinModules(listOf(viewModelModule, repositoryModule))
        }
    }
}