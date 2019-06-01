package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.theater.sort.TheaterSortFragment
import soup.movie.ui.theater.sort.TheaterSortViewModel

@Module
abstract class TheaterSortUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindTheaterSortFragment(): TheaterSortFragment

    @Binds
    @IntoMap
    @ViewModelKey(TheaterSortViewModel::class)
    abstract fun bindTheaterSortViewModel(viewModel: TheaterSortViewModel): ViewModel
}
