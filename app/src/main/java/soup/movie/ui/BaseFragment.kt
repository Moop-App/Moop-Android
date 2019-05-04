package soup.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import soup.movie.util.activityViewModelProvider
import soup.movie.util.lazyFast
import soup.movie.util.parentViewModelProvider
import soup.movie.util.viewModelProvider
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    // Handling ViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel> activityViewModel(): Lazy<VM> =
        lazyFast { activityViewModelProvider<VM>(viewModelFactory) }

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> =
        lazyFast { viewModelProvider<VM>(viewModelFactory) }

    protected inline fun <reified VM : ViewModel> parentViewModel(): Lazy<VM> =
        lazyFast { parentViewModelProvider<VM>(viewModelFactory) }
}
