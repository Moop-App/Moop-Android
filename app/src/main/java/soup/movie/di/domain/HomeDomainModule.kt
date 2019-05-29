package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.data.MoopRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.filter.GetGenreListUseCase
import soup.movie.domain.main.GetMovieFilterUseCase
import soup.movie.domain.main.GetNowMovieListUseCase
import soup.movie.domain.main.GetPlanMovieListUseCase
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting

@Module
class HomeDomainModule {

    @ActivityScope
    @Provides
    fun provideGetNowMovieListUseCase(
        repository: MoopRepository
    ): GetNowMovieListUseCase = GetNowMovieListUseCase(repository)

    @ActivityScope
    @Provides
    fun provideGetPlanMovieListUseCase(
        repository: MoopRepository
    ): GetPlanMovieListUseCase = GetPlanMovieListUseCase(repository)

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
    fun provideGetGenreListUseCase(
        repository: MoopRepository
    ): GetGenreListUseCase = GetGenreListUseCase(repository)
}
