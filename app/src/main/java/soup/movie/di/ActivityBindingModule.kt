package soup.movie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.ActivityScope
import soup.movie.di.ui.*
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.detail.timetable.TimetableActivity
import soup.movie.ui.main.MainActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.ui.theater.sort.TheaterSortActivity

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        MainUiModule::class,
        MainTabUiModule::class
    ])
    internal abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        DetailUiModule::class
    ])
    internal abstract fun detailActivity(): DetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        TimetableUiModule::class
    ])
    internal abstract fun timetableActivity(): TimetableActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        TheaterSortUiModule::class
    ])
    internal abstract fun theaterSortActivity(): TheaterSortActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        TheaterEditUiModule::class
    ])
    internal abstract fun theaterEditActivity(): TheaterEditActivity
}
