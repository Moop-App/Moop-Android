package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.settings.AppSettings

@Module
class HomeDomainModule {

    @ActivityScope
    @Provides
    fun provideGetMovieFilterUseCase(
        appSettings: AppSettings
    ): GetMovieFilterUseCase {
        return GetMovieFilterUseCase(appSettings)
    }
}
