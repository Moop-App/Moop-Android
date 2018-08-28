package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.ui.detail.DetailContract
import soup.movie.ui.detail.DetailPresenter

@Module
class DetailUiModule {

    @ActivityScope
    @Provides
    fun presenter(): DetailContract.Presenter {
        return DetailPresenter()
    }
}
