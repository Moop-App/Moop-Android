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
package soup.movie.home.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import soup.movie.analytics.EventAnalytics
import soup.movie.home.HomeFragmentDirections
import javax.inject.Inject

abstract class HomeContentsFragment : HomeTabFragment() {

    @Inject
    lateinit var analytics: EventAnalytics

    protected abstract val viewModel: HomeContentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    val isLoading by viewModel.isLoading.observeAsState(false)
                    val isError by viewModel.isError.observeAsState(false)
                    val movies by viewModel.movies.observeAsState(emptyList())
                    HomeContentsScreen(
                        movies = movies,
                        onItemClick = { movie ->
                            analytics.clickMovie()
                            findNavController().navigate(HomeFragmentDirections.actionToDetail(movie))
                        },
                        isLoading = isLoading,
                        isError = isError,
                        onErrorClick = {
                            viewModel.refresh()
                        }
                    )
                }
            }
        }
    }
}
