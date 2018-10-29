package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import soup.movie.data.MoopRepository
import soup.movie.di.scope.FragmentScope
import soup.movie.settings.impl.*
import soup.movie.ui.main.movie.MovieListContract
import soup.movie.ui.main.movie.filter.MovieFilterContract
import soup.movie.ui.main.movie.filter.MovieFilterFragment
import soup.movie.ui.main.movie.filter.MovieFilterPresenter
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.now.NowPresenter
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.plan.PlanPresenter
import soup.movie.ui.main.settings.SettingsContract
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.ui.main.settings.SettingsPresenter
import soup.movie.ui.main.settings.help.HelpContract
import soup.movie.ui.main.settings.help.HelpFragment
import soup.movie.ui.main.settings.help.HelpPresenter
import soup.movie.ui.main.theaters.TheatersContract
import soup.movie.ui.main.theaters.TheatersFragment
import soup.movie.ui.main.theaters.TheatersPresenter

@Module
abstract class MainTabUiModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        NowModule::class
    ])
    internal abstract fun provideNowFragment(): NowFragment

    @Module
    class NowModule {

        @FragmentScope
        @Provides
        fun presenter(theaterFilterSetting: TheaterFilterSetting,
                      ageFilterSetting: AgeFilterSetting,
                      repository: MoopRepository):
                MovieListContract.Presenter =
                NowPresenter(theaterFilterSetting, ageFilterSetting, repository)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        PlanModule::class
    ])
    internal abstract fun providePlanFragment(): PlanFragment

    @Module
    class PlanModule {

        @FragmentScope
        @Provides
        fun presenter(theaterFilterSetting: TheaterFilterSetting,
                      ageFilterSetting: AgeFilterSetting,
                      repository: MoopRepository):
                MovieListContract.Presenter =
                PlanPresenter(theaterFilterSetting, ageFilterSetting, repository)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        MovieFilterModule::class
    ])
    internal abstract fun provideMovieFilterFragment(): MovieFilterFragment

    @Module
    class MovieFilterModule {

        @FragmentScope
        @Provides
        fun presenter(theaterFilterSetting: TheaterFilterSetting,
                      ageFilterSetting: AgeFilterSetting):
                MovieFilterContract.Presenter =
                MovieFilterPresenter(theaterFilterSetting, ageFilterSetting)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        TheatersModule::class
    ])
    internal abstract fun provideTheatersFragment(): TheatersFragment

    @Module
    class TheatersModule {

        @FragmentScope
        @Provides
        fun presenter(repository: MoopRepository,
                      theatersSetting: TheatersSetting):
                TheatersContract.Presenter =
                TheatersPresenter(repository, theatersSetting)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        SettingsModule::class
    ])
    internal abstract fun provideSettingsFragment(): SettingsFragment

    @Module
    class SettingsModule {

        @FragmentScope
        @Provides
        fun presenter(theatersSetting: TheatersSetting,
                      usePaletteThemeSetting: UsePaletteThemeSetting,
                      useWebLinkSetting: UseWebLinkSetting,
                      repository: MoopRepository):
                SettingsContract.Presenter =
                SettingsPresenter(theatersSetting, usePaletteThemeSetting, useWebLinkSetting, repository)
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        HelpModule::class
    ])
    internal abstract fun provideHelpFragment(): HelpFragment

    @Module
    class HelpModule {

        @FragmentScope
        @Provides
        fun presenter(): HelpContract.Presenter = HelpPresenter()
    }
}
