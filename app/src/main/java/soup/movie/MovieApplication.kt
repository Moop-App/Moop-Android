package soup.movie

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mapbox.mapboxsdk.Mapbox
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import soup.movie.di.DaggerApplicationComponent
import soup.movie.spec.ThemeSpecs
import soup.movie.theme.ThemeBook

class MovieApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        BuildType.init(this)
        initCrashlytics()
        AndroidThreeTen.init(this)
        ThemeBook.initialize(this,
                ThemeSpecs.DEFAULT,
                ThemeSpecs.BLACK,
                ThemeSpecs.WA_TGBH)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }

    private fun initCrashlytics() {
        val core = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }
}
