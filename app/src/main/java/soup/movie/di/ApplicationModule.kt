package soup.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import soup.movie.MovieApplication

@Module
class ApplicationModule {

    @Provides
    fun provideContext(application: MovieApplication): Context {
        return application.applicationContext
    }
}
