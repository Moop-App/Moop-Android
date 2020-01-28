package soup.movie.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import soup.movie.MovieApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityBindingModule::class,
    ViewModelModule::class,
    WorkerModule::class,
    MoopRepositoryModule::class,
    SharedPreferencesModule::class
])
interface ApplicationComponent : AndroidInjector<MovieApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<MovieApplication>
}
