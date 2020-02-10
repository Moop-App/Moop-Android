package soup.movie.search

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ext.AlwaysDiffCallback
import dagger.android.support.DaggerFragment
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import soup.movie.analytics.EventAnalytics
import soup.movie.ext.assistedViewModels
import soup.movie.home.HomeContentsListAdapter
import soup.movie.search.databinding.SearchContentsBinding
import soup.movie.search.databinding.SearchFragmentBinding
import soup.movie.search.databinding.SearchHeaderBinding
import soup.movie.util.ImeUtil
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var analytics: EventAnalytics
    private lateinit var binding: SearchFragmentBinding

    @Inject
    lateinit var viewModelFactory: SearchViewModel.Factory
    private val viewModel: SearchViewModel by assistedViewModels {
        viewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.header.setup()
        binding.contents.setup()
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun SearchFragmentBinding.adaptSystemWindowInset() {
        root.doOnApplyWindowInsets { view, insets, initialState ->
            view.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop
            )
        }
        contents.listView.doOnApplyWindowInsets { listView, insets, initialState ->
            listView.updatePadding(
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
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
        val listAdapter = HomeContentsListAdapter(root.context, AlwaysDiffCallback()) { movie, sharedElements ->
            analytics.clickMovie()
            findNavController().navigate(
                SearchFragmentDirections.actionToDetail(movie),
                ActivityNavigatorExtras(
                    activityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                )
            )
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
