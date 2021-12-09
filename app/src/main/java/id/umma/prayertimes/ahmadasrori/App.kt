package id.umma.prayertimes.ahmadasrori

import android.app.Application
import id.umma.prayertimes.ahmadasrori.di.dataModule
import id.umma.prayertimes.ahmadasrori.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule)
            modules(viewModelModule)
        }
    }
}