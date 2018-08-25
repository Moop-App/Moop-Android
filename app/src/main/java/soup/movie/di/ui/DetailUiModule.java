package soup.movie.di.ui;

import dagger.Module;
import dagger.Provides;
import soup.movie.data.MoobRepository;
import soup.movie.di.scope.ActivityScope;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.detail.DetailContract;
import soup.movie.ui.detail.DetailPresenter;

@Module
public class DetailUiModule {

    @ActivityScope
    @Provides
    DetailContract.Presenter presenter(
            MoobRepository moobRepository,
            TheaterSetting theaterSetting) {
        return new DetailPresenter(moobRepository, theaterSetting);
    }
}
