package soup.movie

import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.DaggerApplicationComponent
import soup.movie.notification.NotificationSpecs
import soup.movie.spec.ThemeSpecs
import soup.movie.theme.ThemeBook

class MovieApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        BuildType.init(this)
        AndroidThreeTen.init(this)
        ThemeBook.initialize(this,
                ThemeSpecs.DEFAULT,
                ThemeSpecs.BLACK)
        NotificationSpecs.initialize(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }
}
