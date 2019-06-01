package soup.movie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.domain.HomeDomainModule
import soup.movie.di.scope.ActivityScope
import soup.movie.di.ui.*
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.main.MainActivity
import soup.movie.ui.theater.edit.TheaterEditActivity

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        MainUiModule::class,
        HomeUiModule::class,
        HomeDomainModule::class,
        SearchUiModule::class,
        SettingsUiModule::class,
        ThemeOptionUiModule::class,
        TheaterSortUiModule::class
    ])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        DetailUiModule::class
    ])
    abstract fun detailActivity(): DetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        TheaterEditManagerUiModule::class,
        TheaterEditUiModule::class,
        TheaterEditTabUiModule::class
    ])
    abstract fun theaterEditActivity(): TheaterEditActivity
}
