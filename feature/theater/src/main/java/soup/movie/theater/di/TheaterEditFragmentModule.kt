package soup.movie.theater.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import soup.movie.model.repository.MoopRepository
import soup.movie.settings.AppSettings
import soup.movie.theater.edit.TheaterEditManager

@Module
@InstallIn(ActivityComponent::class)
class TheaterEditDomainModule {

    @Provides
    @ActivityScoped
    fun provideTheaterEditManager(
        repository: MoopRepository,
        appSettings: AppSettings
    ): TheaterEditManager {
        return TheaterEditManager(
            repository,
            appSettings
        )
    }
}
