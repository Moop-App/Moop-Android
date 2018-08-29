package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.MoobRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.TheaterSetting
import soup.movie.ui.detail.DetailContract
import soup.movie.ui.detail.DetailPresenter
import soup.movie.ui.detail.timetable.TimeTableContract
import soup.movie.ui.detail.timetable.TimeTablePresenter

@Module
class DetailUiModule {

    @ActivityScope
    @Provides
    fun presenter(): DetailContract.Presenter {
        return DetailPresenter()
    }

    @ActivityScope
    @Provides
    fun timeTablePresenter(
            moobRepository: MoobRepository,
            theaterSetting: TheaterSetting): TimeTableContract.Presenter {
        return TimeTablePresenter(moobRepository, theaterSetting)
    }
}
