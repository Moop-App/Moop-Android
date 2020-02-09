package soup.movie.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import soup.movie.MovieApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ApplicationModule::class,
    ActivityBindingModule::class,
    ViewModelModule::class,
    WorkerModule::class,
    DbComponentModule::class,
    ApiComponentModule::class,
    RepositoryComponentModule::class,
    SharedPreferencesModule::class
])
interface ApplicationComponent : AndroidInjector<MovieApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}
