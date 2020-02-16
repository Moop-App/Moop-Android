package soup.movie

import androidx.work.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.createAppComponent
import soup.movie.notification.NotificationChannels
import soup.movie.theme.ThemeOptionManager
import javax.inject.Inject

class MovieApplication : DaggerApplication(), Configuration.Provider {

    @Inject lateinit var workConfiguration: Configuration
    @Inject lateinit var themeOptionManager: ThemeOptionManager

    val applicationComponent by lazy {
        createAppComponent()
    }

    override fun onCreate() {
        super.onCreate()
        BuildType.init(this)
        AndroidThreeTen.init(this)
        NotificationChannels.initialize(this)
        themeOptionManager.initialize()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationComponent
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workConfiguration
    }
}
