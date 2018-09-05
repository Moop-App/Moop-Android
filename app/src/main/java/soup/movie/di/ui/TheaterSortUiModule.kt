package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.theater.sort.TheaterSortContract
import soup.movie.ui.theater.sort.TheaterSortPresenter

@Module
class TheaterSortUiModule {

    @ActivityScope
    @Provides
    internal fun presenter(theaterSetting: TheaterSetting):
            TheaterSortContract.Presenter =
            TheaterSortPresenter(theaterSetting)
}
