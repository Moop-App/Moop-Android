package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.MoopRepository
import soup.movie.data.TheaterEditManager
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.theater.edit.TheaterEditContract
import soup.movie.ui.theater.edit.TheaterEditPresenter

@Module
class TheaterEditUiModule {

    @ActivityScope
    @Provides
    internal fun presenter(manager: TheaterEditManager):
            TheaterEditContract.Presenter =
            TheaterEditPresenter(manager)

    @ActivityScope
    @Provides
    internal fun manager(repository: MoopRepository,
                         theatersSetting: TheatersSetting):
            TheaterEditManager =
            TheaterEditManager(repository, theatersSetting)
}
