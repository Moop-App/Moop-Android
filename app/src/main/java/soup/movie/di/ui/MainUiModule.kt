package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.MainTabSetting
import soup.movie.ui.main.MainContract
import soup.movie.ui.main.MainPresenter

@Module
class MainUiModule {

    @ActivityScope
    @Provides
    internal fun presenter(mainTabSetting: MainTabSetting): MainContract.Presenter =
            MainPresenter(mainTabSetting)
}
