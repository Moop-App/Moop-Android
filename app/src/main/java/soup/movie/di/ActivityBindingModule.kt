package soup.movie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.domain.MainDomainModule
import soup.movie.di.scope.ActivityScope
import soup.movie.di.ui.*
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.main.MainActivity
import soup.movie.ui.search.SearchActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.ui.theater.sort.TheaterSortActivity
import soup.movie.ui.theme.ThemeOptionActivity

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        MainUiModule::class,
        MainTabUiModule::class,
        MainDomainModule::class
    ])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        SearchUiModule::class
    ])
    abstract fun searchActivity(): SearchActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        DetailUiModule::class
    ])
    abstract fun detailActivity(): DetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        TheaterSortUiModule::class
    ])
    abstract fun theaterSortActivity(): TheaterSortActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        TheaterEditManagerUiModule::class,
        TheaterEditUiModule::class,
        TheaterEditTabUiModule::class
    ])
    abstract fun theaterEditActivity(): TheaterEditActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ThemeOptionUiModule::class
    ])
    abstract fun bindThemeOptionActivity(): ThemeOptionActivity
}
