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
package soup.movie.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.ext.showToast
import soup.movie.ext.startActivitySafely
import soup.movie.model.Theater
import soup.movie.system.SystemViewModel
import soup.movie.ui.MovieTheme
import soup.movie.util.Cgv
import soup.movie.util.LotteCinema
import soup.movie.util.Megabox
import soup.movie.util.Moop

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val systemViewModel: SystemViewModel by activityViewModels()
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieTheme {
                    val context = LocalContext.current
                    SettingsScreen(
                        systemViewModel = systemViewModel,
                        onThemeEditClick = {
                            onThemeEditClicked()
                        },
                        onTheaterItemClick = { theater ->
                            theater.executeWeb(context)
                        },
                        onTheaterEditClick = {
                            onTheaterEditClicked()
                        },
                        onVersionClick = {
                            if (it.isLatest) {
                                context.showToast(R.string.settings_version_toast)
                            } else {
                                Moop.executePlayStore(requireContext())
                            }
                        },
                        onMarketIconClick = {
                            Moop.executePlayStore(context)
                        },
                        onBugReportClick = {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse("mailto:")
                            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(HELP_EMAIL))
                            intent.putExtra(
                                Intent.EXTRA_SUBJECT,
                                "뭅 v${BuildConfig.VERSION_NAME} 버그리포트"
                            )
                            context.startActivitySafely(intent)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.versionUiModel.observe(viewLifecycleOwner) { version ->
            if (version.isLatest.not()) {
                showVersionUpdateDialog()
            }
        }
    }

    private fun showVersionUpdateDialog() {
        AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_round_new_releases)
            .setTitle(R.string.settings_version_update_title)
            .setMessage(getString(R.string.settings_version_update_message))
            .setPositiveButton(R.string.settings_version_update_button_positive) { _, _ ->
                Moop.executePlayStore(requireContext())
            }
            .setNegativeButton(R.string.settings_version_update_button_negative) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun Theater.executeWeb(ctx: Context) {
        return when (type) {
            Theater.TYPE_CGV -> Cgv.executeWeb(ctx, this)
            Theater.TYPE_LOTTE -> LotteCinema.executeWeb(ctx, this)
            Theater.TYPE_MEGABOX -> Megabox.executeWeb(ctx, this)
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }

    private fun onTheaterEditClicked() {
        findNavController().navigate(
            SettingsFragmentDirections.actionToTheaterSort()
        )
    }

    private fun onThemeEditClicked() {
        findNavController().navigate(
            SettingsFragmentDirections.actionToThemeOption()
        )
    }

    companion object {

        private const val HELP_EMAIL = "help.moop@gmail.com"
    }
}
