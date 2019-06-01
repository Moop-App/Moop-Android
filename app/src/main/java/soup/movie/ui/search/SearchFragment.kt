package soup.movie.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.search_fragment.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.databinding.SearchFragmentBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.home.HomeListAdapter
import soup.movie.util.ImeUtil
import soup.movie.util.lazyFast
import soup.movie.util.observe
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    private val viewModel: SearchViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private var focusQuery = true

    private val listAdapter by lazyFast {
        HomeListAdapter { movie, sharedElements ->
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return SearchFragmentBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@SearchFragment.viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView(view.context)
        listView.apply {
            adapter = listAdapter
            itemAnimator = FadeInAnimator().apply {
                addDuration = 0
                removeDuration = 0
            }
        }
        viewModel.uiModel.observe(this) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (focusQuery) {
            ImeUtil.showIme(searchView)
        }
    }

    override fun onPause() {
        super.onPause()
        ImeUtil.hideIme(searchView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        focusQuery = false
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun render(viewState: SearchUiModel) {
        loadingView.isVisible = viewState is SearchUiModel.LoadingState
        noItemsView.isVisible = viewState.hasNoItems()
        if (viewState is SearchUiModel.DoneState) {
            listAdapter.submitList(viewState.items)
        }
    }

    private fun setupSearchView(ctx: Context) {
        val searchManager = ctx.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
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
        searchBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
