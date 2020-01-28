package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.key.ViewModelKey
import soup.movie.di.scope.FragmentScope
import soup.movie.ui.search.SearchFragment
import soup.movie.ui.search.SearchViewModel

@Module
abstract class SearchUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideSearchFragment(): SearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}
