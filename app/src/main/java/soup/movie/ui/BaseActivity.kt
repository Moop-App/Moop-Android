package soup.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import soup.movie.util.viewModelProvider
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    // Handling ViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> =
        lazy { viewModelProvider<VM>(viewModelFactory) }
}
