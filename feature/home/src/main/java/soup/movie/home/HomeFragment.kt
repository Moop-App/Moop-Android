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
package soup.movie.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.analytics.EventAnalytics
import soup.movie.system.SystemViewModel
import soup.movie.ui.MovieTheme
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var analytics: EventAnalytics

    private val systemViewModel: SystemViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieTheme {
                    HomeScreen(
                        viewModel = viewModel,
                        analytics = analytics,
                        onNavigationClick = {
                            systemViewModel.openNavigationMenu()
                        },
                        onTabSelected = { tab ->
                            viewModel.onTabSelected(tab)
                        },
                        onMovieItemClick = { movie ->
                            analytics.clickMovie()
                            findNavController().navigate(HomeFragmentDirections.actionToDetail(movie))
                        }
                    )
                }
            }
        }
    }
}
