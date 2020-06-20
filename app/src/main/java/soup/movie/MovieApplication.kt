package soup.movie

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.android.play.core.splitcompat.SplitCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import soup.movie.notification.NotificationChannels
import soup.movie.theme.ThemeOptionManager
import soup.movie.util.CrashlyticsTree
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application() {

    @Inject lateinit var themeOptionManager: ThemeOptionManager
    @Inject lateinit var workerFactory: HiltWorkerFactory

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
        WorkManager.initialize(this, Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}
