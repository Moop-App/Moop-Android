package soup.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import soup.movie.MovieApplication
import soup.movie.analytics.EventAnalytics
import soup.movie.util.ImageUriProvider
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideContext(
        application: MovieApplication
    ): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideImageUriProvider(
        context: Context
    ): ImageUriProvider = ImageUriProvider(context)

    @Singleton
    @Provides
    fun provideEventAnalytics(
        context: Context
    ): EventAnalytics = EventAnalytics(context)
}
