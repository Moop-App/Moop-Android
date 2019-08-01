package soup.movie.ui.base

import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    // Handling ViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel> activityViewModels(): Lazy<VM> {
        return activityViewModels { viewModelFactory }
    }

    protected inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        return viewModels { viewModelFactory }
    }

    protected inline fun <reified VM : ViewModel> parentViewModels(): Lazy<VM> {
        return requireParentFragment().viewModels { viewModelFactory }
    }

    protected inline fun <reified VM : ViewModel> navGraphViewModels(@IdRes navGraphId: Int): Lazy<VM> {
        return navGraphViewModels(navGraphId) { viewModelFactory }
    }
}
