package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.source.MoobRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.detail.timetable.TimetableContract
import soup.movie.ui.detail.timetable.TimetablePresenter

@Module
class TimeTableUiModule {

    @ActivityScope
    @Provides
    fun timeTablePresenter(moobRepository: MoobRepository,
                           theaterSetting: TheaterSetting):
            TimetableContract.Presenter =
            TimetablePresenter(moobRepository, theaterSetting)
}
