package soup.movie.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import soup.movie.theme.ThemeBook
import soup.movie.util.viewModelProvider
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeBook.open(this)
        super.onCreate(savedInstanceState)
    }

    // Handling ViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> =
        lazy { viewModelProvider<VM>(viewModelFactory) }
}
