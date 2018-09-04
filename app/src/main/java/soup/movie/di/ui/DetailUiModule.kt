package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.util.ImageUriProvider
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.ui.detail.DetailContract
import soup.movie.ui.detail.DetailPresenter

@Module
class DetailUiModule {

    @ActivityScope
    @Provides
    fun presenter(usePaletteThemeSetting: UsePaletteThemeSetting,
                  imageUriProvider: ImageUriProvider): DetailContract.Presenter {
        return DetailPresenter(usePaletteThemeSetting, imageUriProvider)
    }
}
