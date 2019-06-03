package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.data.MoopRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.search.SearchMovieUseCase

@Module
class SearchDomainModule {

    @ActivityScope
    @Provides
    fun provideGetGenreListUseCase(
        repository: MoopRepository
    ): SearchMovieUseCase = SearchMovieUseCase(repository)
}
