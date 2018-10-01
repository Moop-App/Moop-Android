package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import soup.movie.data.MoobRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.detail.timetable.TimetableContract
import soup.movie.ui.detail.timetable.TimetablePresenter

@Module
class TimetableUiModule {

    @ActivityScope
    @Provides
    fun timeTablePresenter(moobRepository: MoobRepository,
                           theaterSetting: TheaterSetting):
            TimetableContract.Presenter =
            TimetablePresenter(moobRepository, theaterSetting)
}
