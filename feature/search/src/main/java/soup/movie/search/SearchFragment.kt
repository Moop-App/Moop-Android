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
package soup.movie.search

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import soup.movie.analytics.EventAnalytics
import soup.movie.home.HomeContentsListAdapter
import soup.movie.search.databinding.SearchContentsBinding
import soup.movie.search.databinding.SearchFragmentBinding
import soup.movie.search.databinding.SearchHeaderBinding
import soup.movie.util.ImeUtil
import soup.movie.util.autoCleared
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var analytics: EventAnalytics

    private var binding: SearchFragmentBinding by autoCleared()

    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SearchFragmentBinding.bind(view).apply {
            header.setup()
            contents.setup()
            adaptSystemWindowInset()
        }
    }

    private fun SearchFragmentBinding.adaptSystemWindowInset() {
        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.updatePadding(
                top = initialState.paddings.top + insets.getInsets(systemBars()).top
            )
        }
            .applyToView(root)
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updatePadding(
                    bottom = initialState.paddings.bottom + insets.getInsets(systemBars()).bottom
                )
            }
            .applyToView(contents.listView)
    }

    override fun onResume() {
        super.onResume()
        ImeUtil.showIme(binding.header.searchView)
    }

    override fun onPause() {
        super.onPause()
        ImeUtil.hideIme(binding.header.searchView)
    }

    private fun SearchHeaderBinding.setup() {
        searchView.queryHint = getString(R.string.search_hint)
        searchView.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        searchView.imeOptions = searchView.imeOptions or
            EditorInfo.IME_ACTION_SEARCH or
            EditorInfo.IME_FLAG_NO_EXTRACT_UI or
            EditorInfo.IME_FLAG_NO_FULLSCREEN
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            private var lastQuery = ""

            override fun onQueryTextSubmit(query: String): Boolean {
                ImeUtil.hideIme(searchView)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                val searchText = query.trim()
                if (searchText == lastQuery) {
                    return false
                }

                lastQuery = searchText

                lifecycleScope.launch {
                    delay(300)
                    if (searchText == lastQuery) {
                        viewModel.searchFor(query)
                    }
                }
                return true
            }
        })
        searchBack.setOnDebounceClickListener {
            findNavController().navigateUp()
        }
    }

    private fun SearchContentsBinding.setup() {
        val listAdapter = HomeContentsListAdapter(root.context, AlwaysDiffCallback()) { movie ->
            analytics.clickMovie()
            findNavController().navigate(SearchFragmentDirections.actionToDetail(movie))
        }
        listView.apply {
            adapter = listAdapter
            itemAnimator?.apply {
                addDuration = 300
                changeDuration = 0
                moveDuration = 0
                removeDuration = 300
            }
        }
        viewModel.uiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.movies)
            noItemsView.isVisible = it.hasNoItem
        }
    }
}
