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
package soup.movie.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.analytics.EventAnalytics
import soup.movie.home.HomeFragmentDirections
import javax.inject.Inject

@AndroidEntryPoint
class HomeFavoriteFragment : Fragment() {

    @Inject
    lateinit var analytics: EventAnalytics

    private val viewModel: HomeFavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    val movies by viewModel.movies.observeAsState(emptyList())
                    HomeFavoriteScreen(
                        movies = movies,
                        onItemClick = { movie ->
                            analytics.clickMovie()
                            findNavController().navigate(HomeFragmentDirections.actionToDetail(movie))
                        }
                    )
                }
            }
        }
    }
}
