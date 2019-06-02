package soup.movie.di.domain

import dagger.Module
import dagger.Provides
import soup.movie.data.MoopRepository
import soup.movie.di.scope.ActivityScope
import soup.movie.domain.theater.edit.TheaterEditManager
import soup.movie.settings.impl.TheatersSetting

@Module
class TheaterEditDomainModule {

    @ActivityScope
    @Provides
    fun provideTheaterEditManager(
        repository: MoopRepository,
        theatersSetting: TheatersSetting
    ): TheaterEditManager = TheaterEditManager(repository, theatersSetting)
}
