package soup.movie.di.ui;

import dagger.Module;
import dagger.Provides;
import soup.movie.di.scope.ActivityScope;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.theater.sort.TheaterSortContract;
import soup.movie.ui.theater.sort.TheaterSortPresenter;

@Module
public class TheaterSortUiModule {

    @ActivityScope
    @Provides
    TheaterSortContract.Presenter presenter(TheaterSetting theaterSetting) {
        return new TheaterSortPresenter(theaterSetting);
    }
}
