package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.data.MoopRepository
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.main.MainViewModel
import soup.movie.ui.main.movie.MovieListContract
import soup.movie.ui.main.movie.filter.MovieFilterFragment
import soup.movie.ui.main.movie.filter.MovieFilterViewModel
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.now.NowPresenter
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.plan.PlanPresenter
import soup.movie.ui.main.settings.SettingsContract
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.ui.main.settings.SettingsPresenter

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
    @ContributesAndroidInjector(modules = [
        NowModule::class
    ])
    abstract fun provideNowFragment(): NowFragment

    @Module
    class NowModule {

        @FragmentScope
        @Provides
        fun presenter(
            theaterFilterSetting: TheaterFilterSetting,
            ageFilterSetting: AgeFilterSetting,
            repository: MoopRepository
        ): MovieListContract.Presenter = NowPresenter(theaterFilterSetting, ageFilterSetting, repository)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        PlanModule::class
    ])
    abstract fun providePlanFragment(): PlanFragment

    @Module
    class PlanModule {

        @FragmentScope
        @Provides
        fun presenter(
            theaterFilterSetting: TheaterFilterSetting,
            ageFilterSetting: AgeFilterSetting,
            repository: MoopRepository
        ): MovieListContract.Presenter = PlanPresenter(theaterFilterSetting, ageFilterSetting, repository)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        SettingsModule::class
    ])
    abstract fun provideSettingsFragment(): SettingsFragment

    @Module
    class SettingsModule {

        @FragmentScope
        @Provides
        fun presenter(
            theatersSetting: TheatersSetting,
            repository: MoopRepository
        ): SettingsContract.Presenter = SettingsPresenter(theatersSetting, repository)
    }

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideMovieFilterFragment(): MovieFilterFragment

    @Binds
    @IntoMap
    @ViewModelKey(MovieFilterViewModel::class)
    abstract fun bindMovieFilterViewModel(viewModel: MovieFilterViewModel): ViewModel
}
