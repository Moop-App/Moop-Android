package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.source.MoobRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.LastMainTabSetting
import soup.movie.ui.main.MainContract
import soup.movie.ui.main.MainPresenter

@Module
class MainUiModule {

    @ActivityScope
    @Provides
    internal fun presenter(lastMainTabSetting: LastMainTabSetting,
                           repository: MoobRepository):
            MainContract.Presenter =
            MainPresenter(lastMainTabSetting, repository)
}
