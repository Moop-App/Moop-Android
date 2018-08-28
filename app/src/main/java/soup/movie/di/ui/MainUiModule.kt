package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.ui.main.MainContract
import soup.movie.ui.main.MainPresenter

@Module
class MainUiModule {

    @ActivityScope
    @Provides
    internal fun presenter(): MainContract.Presenter {
        return MainPresenter()
    }
}
