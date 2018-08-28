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
    MoobRepositoryModule::class,
    SharedPreferencesModule::class
])
interface ApplicationComponent : AndroidInjector<MovieApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MovieApplication>()
}
