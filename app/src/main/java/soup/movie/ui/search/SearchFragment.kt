package soup.movie.ui.search

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
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.SearchContentsBinding
import soup.movie.databinding.SearchFragmentBinding
import soup.movie.databinding.SearchHeaderBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.home.HomeListAdapter
import soup.movie.ui.home.MovieSelectManager
import soup.movie.util.ImeUtil
import soup.movie.util.doOnApplyWindowInsets
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    @Inject
    lateinit var analytics: EventAnalytics
    private lateinit var binding: SearchFragmentBinding

    private val viewModel: SearchViewModel by viewModels()

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
        root.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            view.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop
            )
            contents.listView.updatePadding(
                bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom
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
            override fun onQueryTextSubmit(query: String): Boolean {
                ImeUtil.hideIme(searchView)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                viewModel.searchFor(query)
                return true
            }
        })
        searchBack.setOnDebounceClickListener {
            findNavController().navigateUp()
        }
    }

    private fun SearchContentsBinding.setup() {
        val listAdapter = HomeListAdapter { movie, sharedElements ->
            analytics.clickMovie()
            MovieSelectManager.select(movie)
            findNavController().navigate(
                SearchFragmentDirections.actionToDetail(),
                ActivityNavigatorExtras(
                    activityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                )
            )
        }
        listView.apply {
            adapter = listAdapter
            itemAnimator = FadeInAnimator().apply {
                addDuration = 0
                removeDuration = 0
            }
        }
        viewModel.uiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.movies)
            loadingView.isVisible = it.isLoading
            noItemsView.isVisible = it.hasNoItem
            noItemsView.isVisible = it.hasNoItem
        }
    }
}
