package soup.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import soup.movie.ui.helper.EventAnalytics
import soup.movie.util.ImageUriProvider
import javax.inject.Singleton

@Module
class UtilsModule {

    @Singleton
    @Provides
    fun provideImageUriProvider(context: Context):
            ImageUriProvider =
            ImageUriProvider(context)

    @Singleton
    @Provides
    fun provideEventAnalytics(context: Context):
            EventAnalytics =
            EventAnalytics(context)
}
