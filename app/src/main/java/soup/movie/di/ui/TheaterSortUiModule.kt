package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.key.ViewModelKey
import soup.movie.di.scope.FragmentScope
import soup.movie.ui.theater.sort.TheaterSortFragment
import soup.movie.ui.theater.sort.TheaterSortViewModel

@Module
interface TheaterSortUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    fun bindTheaterSortFragment(): TheaterSortFragment

    @Binds
    @IntoMap
    @ViewModelKey(TheaterSortViewModel::class)
    fun bindTheaterSortViewModel(viewModel: TheaterSortViewModel): ViewModel
}
