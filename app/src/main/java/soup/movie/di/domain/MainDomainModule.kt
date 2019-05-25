package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.data.MoopRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.GetGenreUseCase
import soup.movie.domain.filter.GetFilterGroupUseCase
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting

@Module
class MainDomainModule {

    @ActivityScope
    @Provides
    fun provideGetGenreUseCase(
        repository: MoopRepository
    ): GetGenreUseCase = GetGenreUseCase(repository)

    @ActivityScope
    @Provides
    fun provideGetFilterGroupUseCase(
        theaterFilterSetting: TheaterFilterSetting,
        ageFilterSetting: AgeFilterSetting,
        genreFilterSetting: GenreFilterSetting
    ): GetFilterGroupUseCase = GetFilterGroupUseCase(
        theaterFilterSetting,
        ageFilterSetting,
        genreFilterSetting
    )
}
