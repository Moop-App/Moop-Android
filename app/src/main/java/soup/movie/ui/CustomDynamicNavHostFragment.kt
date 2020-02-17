package soup.movie.ui

import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManagerFactory
import soup.movie.BuildConfig

class CustomDynamicNavHostFragment : DynamicNavHostFragment() {

    override fun createSplitInstallManager(): SplitInstallManager {
        return if (BuildConfig.USE_FAKE_SPLIT) {
            FakeSplitInstallManagerFactory.create(
                requireContext(),
                requireContext().getExternalFilesDir(null)
            )
        } else {
            SplitInstallManagerFactory.create(requireContext())
        }
    }
}
