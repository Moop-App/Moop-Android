package soup.movie

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.movie.di.DaggerApplicationComponent

class MovieApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        BuildType.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }
}
