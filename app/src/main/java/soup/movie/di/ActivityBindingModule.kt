package soup.movie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.domain.HomeDomainModule
import soup.movie.di.domain.TheaterEditDomainModule
import soup.movie.di.scope.ActivityScope
import soup.movie.di.ui.*
import soup.movie.system.di.SystemAssistedInjectModule
import soup.movie.ui.main.MainActivity

@Module
interface ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainUiModule::class,
            HomeUiModule::class,
            HomeDomainModule::class,
            SearchUiModule::class,
            SettingsUiModule::class,
            ThemeSettingUiModule::class,
            TheaterMapUiModule::class,
            TheaterSortUiModule::class,
            TheaterEditUiModule::class,
            TheaterEditDomainModule::class,
            SystemAssistedInjectModule::class
        ]
    )
    fun bindMainActivity(): MainActivity
}
