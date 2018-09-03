package soup.movie.di.ui

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import soup.movie.data.MoobRepository
import soup.movie.di.scope.FragmentScope
import soup.movie.settings.impl.TheaterSetting
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.ui.main.now.NowContract
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.now.NowPresenter
import soup.movie.ui.main.plan.PlanContract
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.plan.PlanPresenter
import soup.movie.ui.main.settings.SettingsContract
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.ui.main.settings.SettingsPresenter

@Module
abstract class MainTabUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
            modules = [
                NowModule::class
            ]
    )
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
    @ContributesAndroidInjector(
            modules = [
                PlanModule::class
            ]
    )
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
    @ContributesAndroidInjector(
            modules = [
                SettingsModule::class
            ]
    )
    internal abstract fun provideSettingsFragment(): SettingsFragment

    @Module
    class SettingsModule {

        @FragmentScope
        @Provides
        fun presenter(theaterSetting: TheaterSetting,
                      usePaletteThemeSetting: UsePaletteThemeSetting): SettingsContract.Presenter {
            return SettingsPresenter(theaterSetting, usePaletteThemeSetting)
        }
    }
}
