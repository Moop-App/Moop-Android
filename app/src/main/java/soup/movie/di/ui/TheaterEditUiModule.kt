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
import soup.movie.ui.theater.edit.cgv.CgvEditPresenter
import soup.movie.ui.theater.edit.lotte.LotteEditFragment
import soup.movie.ui.theater.edit.lotte.LotteEditPresenter
import soup.movie.ui.theater.edit.megabox.MegaboxEditFragment
import soup.movie.ui.theater.edit.megabox.MegaboxEditPresenter
import soup.movie.ui.theater.edit.tab.TheaterEditChildContract

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
    internal fun manager(
        repository: MoopRepository,
        theatersSetting: TheatersSetting
    ): TheaterEditManager = TheaterEditManager(repository, theatersSetting)
}

@Module
abstract class TheaterEditTabUiModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        CgvModule::class
    ])
    internal abstract fun provideCgvFragment(): CgvEditFragment

    @Module
    class CgvModule {

        @FragmentScope
        @Provides
        fun presenter(
            manager: TheaterEditManager
        ): TheaterEditChildContract.Presenter = CgvEditPresenter(manager)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        LotteModule::class
    ])
    internal abstract fun provideLotteFragment(): LotteEditFragment

    @Module
    class LotteModule {

        @FragmentScope
        @Provides
        fun presenter(
            manager: TheaterEditManager
        ): TheaterEditChildContract.Presenter = LotteEditPresenter(manager)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        MegaboxModule::class
    ])
    internal abstract fun provideMegaboxFragment(): MegaboxEditFragment

    @Module
    class MegaboxModule {

        @FragmentScope
        @Provides
        fun presenter(
            manager: TheaterEditManager
        ): TheaterEditChildContract.Presenter = MegaboxEditPresenter(manager)
    }
}
