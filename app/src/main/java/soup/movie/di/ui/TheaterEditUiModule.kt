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
interface TheaterEditUiModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [TheaterEditTabUiModule::class])
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

    @Binds
    @IntoMap
    @ViewModelKey(CgvEditViewModel::class)
    fun bindCgvEditViewModel(viewModel: CgvEditViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindLotteFragment(): LotteEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(LotteEditViewModel::class)
    fun bindLotteEditViewModel(viewModel: LotteEditViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindMegaboxFragment(): MegaboxEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(MegaboxEditViewModel::class)
    fun bindMegaboxEditViewModel(viewModel: MegaboxEditViewModel): ViewModel
}
