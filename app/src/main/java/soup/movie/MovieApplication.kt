package soup.movie

import com.jakewharton.threetenabp.AndroidThreeTen
import com.naver.maps.map.NaverMapSdk
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.DaggerApplicationComponent
import soup.movie.notification.NotificationSpecs
import soup.movie.theme.ThemeOptionManager
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
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_sdk_client_id))
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }
}
