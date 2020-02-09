package soup.movie.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.FragmentScope
import soup.movie.theme.ThemeSettingFragment
import soup.movie.theme.di.ThemeSettingAssistedInjectModule
import soup.movie.theme.di.ThemeSettingFragmentModule

@Module
interface ThemeSettingUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            ThemeSettingFragmentModule::class,
            ThemeSettingAssistedInjectModule::class
        ]
    )
    fun themeSettingFragment(): ThemeSettingFragment
}
