package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting

@Module
class HomeDomainModule {

    @ActivityScope
    @Provides
    fun provideGetMovieFilterUseCase(
        theaterFilterSetting: TheaterFilterSetting,
        ageFilterSetting: AgeFilterSetting,
        genreFilterSetting: GenreFilterSetting
    ): GetMovieFilterUseCase = GetMovieFilterUseCase(
        theaterFilterSetting,
        ageFilterSetting,
        genreFilterSetting
    )
}
