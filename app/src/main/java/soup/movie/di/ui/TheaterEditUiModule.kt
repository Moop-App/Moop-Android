package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.data.MoopRepository
import soup.movie.data.TheaterEditManager
import soup.movie.di.scope.ActivityScope
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.theater.edit.TheaterEditViewModel
import soup.movie.ui.theater.edit.cgv.CgvEditFragment
import soup.movie.ui.theater.edit.cgv.CgvEditViewModel
import soup.movie.ui.theater.edit.lotte.LotteEditFragment
import soup.movie.ui.theater.edit.lotte.LotteEditViewModel
import soup.movie.ui.theater.edit.megabox.MegaboxEditFragment
import soup.movie.ui.theater.edit.megabox.MegaboxEditViewModel

@Module
abstract class TheaterEditUiModule {

    @Binds
    @IntoMap
    @ViewModelKey(TheaterEditViewModel::class)
    abstract fun bindTheaterEditViewModel(viewModel: TheaterEditViewModel): ViewModel
}

@Module
class TheaterEditManagerUiModule {

    @ActivityScope
    @Provides
    fun manager(
        repository: MoopRepository,
        theatersSetting: TheatersSetting
    ): TheaterEditManager = TheaterEditManager(repository, theatersSetting)
}

@Module
abstract class TheaterEditTabUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCgvFragment(): CgvEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(CgvEditViewModel::class)
    abstract fun bindCgvEditViewModel(viewModel: CgvEditViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideLotteFragment(): LotteEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(LotteEditViewModel::class)
    abstract fun bindLotteEditViewModel(viewModel: LotteEditViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideMegaboxFragment(): MegaboxEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(MegaboxEditViewModel::class)
    abstract fun bindMegaboxEditViewModel(viewModel: MegaboxEditViewModel): ViewModel
}
