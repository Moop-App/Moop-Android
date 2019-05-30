package soup.movie.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import soup.movie.R
import soup.movie.util.activityViewModelProvider
import soup.movie.util.lazyFast
import soup.movie.util.parentViewModelProvider
import soup.movie.util.viewModelProvider
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), HasSupportFragmentInjector {

    // Handling DI of Dagger2

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childFragmentInjector
    }

    // Handling BottomSheet

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener { dlg ->
                if (dlg is BottomSheetDialog) {
                    dispatchOnBottomSheetCreated(dlg)
                }
            }
        }
    }

    private fun dispatchOnBottomSheetCreated(dialog: BottomSheetDialog) {
        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) ?: return
        BottomSheetBehavior.from(bottomSheet).run(::onBottomSheetCreated)
    }

    protected fun onBottomSheetCreated(behavior: BottomSheetBehavior<View>) {
        // Do nothing
    }

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
