package soup.movie.core.imageloading.impl.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import soup.movie.common.IoDispatcher
import soup.movie.core.imageloading.ImageUriProvider
import soup.movie.core.imageloading.impl.ImageUriProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreImageLoadingModule {

    @Singleton
    @Provides
    fun provideImageUriProvider(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): ImageUriProvider {
        return ImageUriProviderImpl(context, ioDispatcher)
    }
}
