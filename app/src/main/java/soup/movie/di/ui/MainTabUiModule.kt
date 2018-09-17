package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import soup.movie.data.MoobRepository
import soup.movie.di.scope.FragmentScope
import soup.movie.settings.impl.TheaterSetting
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.settings.impl.UseWebLinkSetting
import soup.movie.ui.main.now.NowContract
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.now.NowPresenter
import soup.movie.ui.main.plan.PlanContract
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
        fun presenter(moobRepository: MoobRepository): NowContract.Presenter {
            return NowPresenter(moobRepository)
        }
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
        fun presenter(moobRepository: MoobRepository): PlanContract.Presenter {
            return PlanPresenter(moobRepository)
        }
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
        fun presenter(repository: MoobRepository,
                      theaterSetting: TheaterSetting):
                TheatersContract.Presenter =
                TheatersPresenter(repository, theaterSetting)
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
        fun presenter(theaterSetting: TheaterSetting,
                      usePaletteThemeSetting: UsePaletteThemeSetting,
                      useWebLinkSetting: UseWebLinkSetting,
                      repository: MoobRepository):
                SettingsContract.Presenter =
                SettingsPresenter(theaterSetting, usePaletteThemeSetting, useWebLinkSetting, repository)
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
