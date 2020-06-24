package soup.movie

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import soup.movie.notification.NotificationChannels
import soup.movie.theme.ThemeOptionManager
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application() {

    @Inject lateinit var themeOptionManager: ThemeOptionManager

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        NotificationChannels.initialize(this)
        themeOptionManager.initialize()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}
