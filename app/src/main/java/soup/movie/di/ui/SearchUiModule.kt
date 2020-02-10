package soup.movie.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.FragmentScope
import soup.movie.search.SearchFragment
import soup.movie.search.di.SearchAssistedInjectModule
import soup.movie.search.di.SearchFragmentModule

@Module
interface SearchUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            SearchFragmentModule::class,
            SearchAssistedInjectModule::class
        ]
    )
    fun bindSearchFragment(): SearchFragment
}
