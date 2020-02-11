package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.key.ViewModelKey
import soup.movie.di.scope.ChildFragmentScope
import soup.movie.di.scope.FragmentScope
import soup.movie.model.repository.MoopRepository
import soup.movie.settings.AppSettings
import soup.movie.ui.theater.edit.*

@Module
interface TheaterEditUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TheaterEditTabUiModule::class,
            TheaterEditDomainModule::class
        ]
    )
    fun bindTheaterEditFragment(): TheaterEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(TheaterEditViewModel::class)
    fun bindTheaterEditViewModel(viewModel: TheaterEditViewModel): ViewModel
}

@Module
interface TheaterEditTabUiModule {

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindCgvFragment(): CgvEditFragment

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindLotteFragment(): LotteEditFragment

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindMegaboxFragment(): MegaboxEditFragment
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
