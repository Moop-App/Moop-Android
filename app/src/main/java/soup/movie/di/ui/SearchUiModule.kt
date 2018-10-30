package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.MoopRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.ui.search.SearchContract
import soup.movie.ui.search.SearchPresenter

@Module
class SearchUiModule {

    @ActivityScope
    @Provides
    fun presenter(repository: MoopRepository):
            SearchContract.Presenter =
            SearchPresenter(repository)
}
