package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.data.MoopRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.filter.GetGenreUseCase
import soup.movie.domain.main.GetMovieFilterUseCase
import soup.movie.domain.main.GetNowMovieUseCase
import soup.movie.domain.main.GetPlanMovieUseCase
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting

@Module
class MainDomainModule {

    @ActivityScope
    @Provides
    fun provideGetNowMovieUseCase(
        repository: MoopRepository
    ): GetNowMovieUseCase = GetNowMovieUseCase(repository)

    @ActivityScope
    @Provides
    fun provideGetPlanMovieUseCase(
        repository: MoopRepository
    ): GetPlanMovieUseCase = GetPlanMovieUseCase(repository)

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

    @ActivityScope
    @Provides
    fun provideGetGenreUseCase(
        repository: MoopRepository
    ): GetGenreUseCase = GetGenreUseCase(repository)
}
