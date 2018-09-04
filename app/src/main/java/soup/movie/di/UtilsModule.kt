package soup.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import soup.movie.util.ImageUriProvider
import javax.inject.Singleton

@Module
class UtilsModule {

    @Singleton
    @Provides
    fun provideImageUriProvider(context: Context): ImageUriProvider =
            ImageUriProvider(context)
}
