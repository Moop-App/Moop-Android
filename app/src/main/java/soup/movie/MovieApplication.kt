package soup.movie

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.android.play.core.splitcompat.SplitCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.createAppComponent
import soup.movie.notification.NotificationChannels
import soup.movie.theme.ThemeOptionManager
import soup.movie.util.CrashlyticsTree
import timber.log.Timber
import javax.inject.Inject

class MovieApplication : DaggerApplication() {

    @Inject lateinit var workConfiguration: Configuration
    @Inject lateinit var themeOptionManager: ThemeOptionManager

    val applicationComponent by lazy {
        createAppComponent()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
        AndroidThreeTen.init(this)
        NotificationChannels.initialize(this)
        themeOptionManager.initialize()
        WorkManager.initialize(this, workConfiguration)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationComponent
    }
}
