package soup.movie.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.navigation.dynamicfeatures.fragment.ui.AbstractProgressFragment
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.databinding.CustomProgressFragmentBinding
import soup.movie.util.LauncherIcons
import timber.log.Timber

class CustomProgressFragment : AbstractProgressFragment(R.layout.custom_progress_fragment) {

    private lateinit var binding: CustomProgressFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = CustomProgressFragmentBinding.bind(view)
        binding.progressIcon.setActivityIcon()
        binding.appLogo.doOnApplyWindowInsets { appLogo, insets, initialState ->
            appLogo.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = initialState.margins.bottom + insets.systemWindowInsetBottom
            }
        }
    }

    private fun ImageView.setActivityIcon() {
        setImageDrawable(LauncherIcons(context).getAppIcon(context, BuildConfig.APPLICATION_ID))
    }

    override fun onProgress(status: Int, bytesDownloaded: Long, bytesTotal: Long) {
        with(binding.installationProgress) {
            isVisible = true
            if (bytesTotal == 0L) {
                isIndeterminate = true
            } else {
                progress = (PROGRESS_MAX * bytesDownloaded / bytesTotal).toInt()
                isIndeterminate = false
            }
        }
        binding.animationView.isVisible = true
    }

    override fun onCancelled() {
        displayErrorState(R.string.installation_cancelled)
        displayAction(R.string.retry) { navigate() }
    }

    override fun onFailed(@SplitInstallErrorCode errorCode: Int) {
        Timber.w("Installation failed with error $errorCode")
        displayErrorState(R.string.installation_failed)
        displayAction(R.string.ok) { findNavController().popBackStack() }
    }

    /**
     * Display an error state message.
     */
    private fun displayErrorState(@StringRes text: Int) {
        binding.run {
            animationView.isInvisible = true
            progressTitle.setText(text)
            progressTitle.isVisible = true
            installationProgress.isInvisible = true
        }
    }

    /**
     * Display the action button and assign `onClick` behavior.
     */
    private fun displayAction(@StringRes text: Int, onClick: () -> Unit) {
        with(binding.progressAction) {
            setText(text)
            setOnClickListener {
                onClick()
            }
            visibility = View.VISIBLE
        }
    }

    companion object {
        private const val PROGRESS_MAX = 100
    }
}
