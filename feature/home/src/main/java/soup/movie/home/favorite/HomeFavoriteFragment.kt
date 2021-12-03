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
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.home.HomeFragmentDirections
import soup.movie.home.R
import soup.movie.home.databinding.HomeTabFavoriteBinding
import soup.movie.home.tab.HomeTabFragment
import soup.movie.util.autoCleared
import javax.inject.Inject

@AndroidEntryPoint
class HomeFavoriteFragment : HomeTabFragment(R.layout.home_tab_favorite) {

    @Inject
    lateinit var analytics: EventAnalytics

    private var binding: HomeTabFavoriteBinding by autoCleared()

    private val viewModel: HomeFavoriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeTabFavoriteBinding.bind(view).apply {
            initViewState(viewModel)
        }
    }

    private fun HomeTabFavoriteBinding.initViewState(viewModel: HomeFavoriteViewModel) {
        val listAdapter = HomeFavoriteListAdapter(root.context) { movie ->
            analytics.clickMovie()
            findNavController().navigate(HomeFragmentDirections.actionToDetail(movie))
        }
        listView.apply {
            adapter = listAdapter
            itemAnimator = FadeInAnimator()
        }
        viewModel.contentsUiModel.observe(viewLifecycleOwner) {
            noItemsView.isVisible = it.movies.isEmpty()
            listAdapter.submitList(it.movies)
        }
    }

    override fun scrollToTop() {
        getListView()?.scrollToTopInternal()
    }

    override fun onBackPressed(): Boolean {
        return getListView()?.scrollToTopInternal() ?: false
    }

    private fun getListView(): RecyclerView? {
        return binding.listView
    }
}
