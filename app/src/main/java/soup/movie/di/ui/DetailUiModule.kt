package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.ui.detail.DetailContract
import soup.movie.ui.detail.DetailPresenter
import soup.movie.util.ImageUriProvider

@Module
class DetailUiModule {

    @ActivityScope
    @Provides
    fun presenter(imageUriProvider: ImageUriProvider):
            DetailContract.Presenter =
            DetailPresenter(imageUriProvider)
}
