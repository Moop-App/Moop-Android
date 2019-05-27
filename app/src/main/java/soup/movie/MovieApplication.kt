package soup.movie

import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.theme.ThemeOptionManager
import soup.movie.di.DaggerApplicationComponent
import soup.movie.notification.NotificationSpecs
import javax.inject.Inject

class MovieApplication : DaggerApplication() {

    @Inject
    lateinit var themeOptionManager: ThemeOptionManager

    override fun onCreate() {
        super.onCreate()
        BuildType.init(this)
        AndroidThreeTen.init(this)
        NotificationSpecs.initialize(this)
        themeOptionManager.initialize()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }
}
