package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.MoobRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.theater.edit.TheaterEditContract
import soup.movie.ui.theater.edit.TheaterEditPresenter

@Module
class TheaterEditUiModule {

    @ActivityScope
    @Provides
    internal fun presenter(moobRepository: MoobRepository,
                           theatersSetting: TheatersSetting):
            TheaterEditContract.Presenter =
            TheaterEditPresenter(moobRepository, theatersSetting)
}
