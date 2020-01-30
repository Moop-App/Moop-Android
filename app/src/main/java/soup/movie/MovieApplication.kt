package soup.movie

import androidx.work.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.DaggerApplicationComponent
import soup.movie.notification.NotificationSpecs
import soup.movie.theme.ThemeOptionManager
import javax.inject.Inject

class MovieApplication : DaggerApplication(), Configuration.Provider {

    @Inject lateinit var workConfiguration: Configuration
    @Inject lateinit var themeOptionManager: ThemeOptionManager

    override fun onCreate() {
        super.onCreate()
        BuildType.init(this)
        AndroidThreeTen.init(this)
        NotificationSpecs.initialize(this)
        themeOptionManager.initialize()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workConfiguration
    }
}
