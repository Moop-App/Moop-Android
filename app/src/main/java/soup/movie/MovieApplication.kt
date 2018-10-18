package soup.movie

import com.jakewharton.threetenabp.AndroidThreeTen
import com.mapbox.mapboxsdk.Mapbox
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.DaggerApplicationComponent
import soup.movie.spec.ThemeSpecs
import soup.movie.theme.ThemeBook

class MovieApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        BuildType.init(this)
        AndroidThreeTen.init(this)
        ThemeBook.initialize(this,
                ThemeSpecs.DEFAULT,
                ThemeSpecs.BLACK,
                ThemeSpecs.WA_TGBH)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }
}
