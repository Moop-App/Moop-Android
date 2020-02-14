package soup.movie.di

import androidx.fragment.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.FragmentScope
import soup.movie.home.HomeFragment
import soup.movie.home.di.HomeAssistedInjectModule
import soup.movie.home.di.HomeFilterFragmentModule
import soup.movie.home.di.HomeFragmentModule
import soup.movie.home.filter.HomeFilterFragment
import soup.movie.search.SearchFragment
import soup.movie.search.di.SearchAssistedInjectModule
import soup.movie.search.di.SearchFragmentModule
import soup.movie.settings.SettingsFragment
import soup.movie.settings.di.SettingsAssistedInjectModule
import soup.movie.settings.di.SettingsFragmentModule
import soup.movie.theater.di.TheaterAssistedInjectModule
import soup.movie.theater.di.TheaterEditDomainModule
import soup.movie.theater.di.TheaterEditFragmentModule
import soup.movie.theater.di.TheaterSortFragmentModule
import soup.movie.theater.edit.TheaterEditFragment
import soup.movie.theater.sort.TheaterSortFragment
import soup.movie.theme.ThemeSettingFragment
import soup.movie.theme.di.ThemeSettingAssistedInjectModule
import soup.movie.theme.di.ThemeSettingFragmentModule
import soup.movie.ui.main.MainActivity
import soup.movie.ui.map.TheaterMapFragment
import soup.movie.ui.map.di.TheaterMapFragmentModule

@Module
interface MainActivityModule {

    @Binds
    fun providesActivity(mainActivity: MainActivity): FragmentActivity

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            HomeFragmentModule::class,
            HomeAssistedInjectModule::class
        ]
    )
    fun bindHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            HomeFilterFragmentModule::class,
            HomeAssistedInjectModule::class
        ]
    )
    fun bindHomeFilterDialogFragment(): HomeFilterFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            SearchFragmentModule::class,
            SearchAssistedInjectModule::class
        ]
    )
    fun bindSearchFragment(): SearchFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            SettingsFragmentModule::class,
            SettingsAssistedInjectModule::class
        ]
    )
    fun bindSettingsFragment(): SettingsFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TheaterSortFragmentModule::class,
            TheaterAssistedInjectModule::class
        ]
    )
    fun bindTheaterSortFragment(): TheaterSortFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TheaterEditFragmentModule::class,
            TheaterEditDomainModule::class,
            TheaterAssistedInjectModule::class
        ]
    )
    fun bindTheaterEditFragment(): TheaterEditFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            ThemeSettingFragmentModule::class,
            ThemeSettingAssistedInjectModule::class
        ]
    )
    fun themeSettingFragment(): ThemeSettingFragment

    //TODO: Move into :feature:theatermap
    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TheaterMapFragmentModule::class
        ]
    )
    fun bindTheaterMapFragment(): TheaterMapFragment

    @Module
    abstract class MainActivityBuilder {

        @ContributesAndroidInjector(
            modules = [
                MainActivityModule::class
            ]
        )
        abstract fun contributeMainActivity(): MainActivity
    }
}
