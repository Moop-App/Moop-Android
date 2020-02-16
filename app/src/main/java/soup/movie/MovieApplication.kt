package soup.movie

import androidx.work.Configuration
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.createAppComponent
import soup.movie.notification.NotificationChannels
import soup.movie.theme.ThemeOptionManager
import javax.inject.Inject

class MovieApplication : DaggerApplication() {

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
        WorkManager.initialize(this, workConfiguration)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationComponent
    }
}
