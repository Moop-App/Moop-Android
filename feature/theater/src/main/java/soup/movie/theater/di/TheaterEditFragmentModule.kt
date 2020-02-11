package soup.movie.theater.di

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import soup.movie.model.repository.MoopRepository
import soup.movie.settings.AppSettings
import soup.movie.theater.edit.CgvEditFragment
import soup.movie.theater.edit.LotteEditFragment
import soup.movie.theater.edit.MegaboxEditFragment
import soup.movie.theater.edit.TheaterEditManager

@Module
abstract class TheaterEditFragmentModule {

    @ContributesAndroidInjector(
        modules = [TheaterAssistedInjectModule::class]
    )
    abstract fun bindCgvFragment(): CgvEditFragment

    @ContributesAndroidInjector(
        modules = [TheaterAssistedInjectModule::class]
    )
    abstract fun bindLotteFragment(): LotteEditFragment

    @ContributesAndroidInjector(
        modules = [TheaterAssistedInjectModule::class]
    )
    abstract fun bindMegaboxFragment(): MegaboxEditFragment
}

@Module
class TheaterEditDomainModule {

    @Provides
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
