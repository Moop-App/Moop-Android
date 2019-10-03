package soup.movie.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import soup.movie.R
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), HasAndroidInjector {

    // Handling DI of Dagger2

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
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
