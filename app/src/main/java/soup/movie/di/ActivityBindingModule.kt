package soup.movie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.domain.HomeDomainModule
import soup.movie.di.domain.SearchDomainModule
import soup.movie.di.domain.TheaterEditDomainModule
import soup.movie.di.scope.ActivityScope
import soup.movie.di.ui.*
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.main.MainActivity

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        MainUiModule::class,
        HomeUiModule::class,
        HomeDomainModule::class,
        SearchUiModule::class,
        SearchDomainModule::class,
        SettingsUiModule::class,
        ThemeOptionUiModule::class,
        TheaterSortUiModule::class,
        TheaterEditUiModule::class,
        TheaterEditDomainModule::class
    ])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        DetailUiModule::class
    ])
    abstract fun detailActivity(): DetailActivity
}
