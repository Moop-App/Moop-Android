package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.MoobRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.TheaterSetting
import soup.movie.ui.theater.edit.TheaterEditContract
import soup.movie.ui.theater.edit.TheaterEditPresenter

@Module
class TheaterEditUiModule {

    @ActivityScope
    @Provides
    internal fun presenter(
            moobRepository: MoobRepository,
            theaterSetting: TheaterSetting): TheaterEditContract.Presenter {
        return TheaterEditPresenter(moobRepository, theaterSetting)
    }
}
