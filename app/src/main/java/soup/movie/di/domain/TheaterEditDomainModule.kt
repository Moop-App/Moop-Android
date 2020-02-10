package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.theater.edit.TheaterEditManager
import soup.movie.model.repository.MoopRepository
import soup.movie.settings.AppSettings

@Module
class TheaterEditDomainModule {

    @ActivityScope
    @Provides
    fun provideTheaterEditManager(
        repository: MoopRepository,
        appSettings: AppSettings
    ): TheaterEditManager {
        return TheaterEditManager(repository, appSettings)
    }
}
