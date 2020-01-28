package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.key.ViewModelKey
import soup.movie.di.scope.ChildFragmentScope
import soup.movie.di.scope.FragmentScope
import soup.movie.ui.theater.edit.TheaterEditFragment
import soup.movie.ui.theater.edit.TheaterEditViewModel
import soup.movie.ui.theater.edit.cgv.CgvEditFragment
import soup.movie.ui.theater.edit.cgv.CgvEditViewModel
import soup.movie.ui.theater.edit.lotte.LotteEditFragment
import soup.movie.ui.theater.edit.lotte.LotteEditViewModel
import soup.movie.ui.theater.edit.megabox.MegaboxEditFragment
import soup.movie.ui.theater.edit.megabox.MegaboxEditViewModel

@Module
abstract class TheaterEditUiModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        TheaterEditTabUiModule::class
    ])
    abstract fun bindTheaterEditFragment(): TheaterEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(TheaterEditViewModel::class)
    abstract fun bindTheaterEditViewModel(viewModel: TheaterEditViewModel): ViewModel
}

@Module
abstract class TheaterEditTabUiModule {

    @ChildFragmentScope
    @ContributesAndroidInjector
    abstract fun provideCgvFragment(): CgvEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(CgvEditViewModel::class)
    abstract fun bindCgvEditViewModel(viewModel: CgvEditViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    abstract fun provideLotteFragment(): LotteEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(LotteEditViewModel::class)
    abstract fun bindLotteEditViewModel(viewModel: LotteEditViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    abstract fun provideMegaboxFragment(): MegaboxEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(MegaboxEditViewModel::class)
    abstract fun bindMegaboxEditViewModel(viewModel: MegaboxEditViewModel): ViewModel
}
