package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.main.MainViewModel
import soup.movie.ui.main.movie.filter.MovieFilterFragment
import soup.movie.ui.main.movie.filter.MovieFilterViewModel
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.now.NowViewModel
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.plan.PlanViewModel
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.ui.main.settings.SettingsViewModel

@Module
abstract class MainUiModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}

@Module
abstract class MainTabUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNowFragment(): NowFragment

    @Binds
    @IntoMap
    @ViewModelKey(NowViewModel::class)
    abstract fun bindNowViewModel(viewModel: NowViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providePlanFragment(): PlanFragment

    @Binds
    @IntoMap
    @ViewModelKey(PlanViewModel::class)
    abstract fun bindPlanViewModel(viewModel: PlanViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideSettingsFragment(): SettingsFragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideMovieFilterFragment(): MovieFilterFragment

    @Binds
    @IntoMap
    @ViewModelKey(MovieFilterViewModel::class)
    abstract fun bindMovieFilterViewModel(viewModel: MovieFilterViewModel): ViewModel
}
