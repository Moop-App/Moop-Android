/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
