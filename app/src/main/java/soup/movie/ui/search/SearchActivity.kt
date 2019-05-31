package soup.movie.ui.search

import android.app.ActivityOptions
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.activity_search.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.databinding.ActivitySearchBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.home.HomeListAdapter
import soup.movie.ui.search.SearchUiModel.DoneState
import soup.movie.ui.search.SearchUiModel.LoadingState
import soup.movie.util.ImeUtil
import soup.movie.util.observe
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    private val viewModel: SearchViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private var focusQuery = true

    private val listAdapter by lazy {
        HomeListAdapter { movie, sharedElements ->
            analytics.clickMovie(isNow = movie.isNow)
            MovieSelectManager.select(movie)
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, 0, ActivityOptions
                .makeSceneTransitionAnimation(this, *sharedElements)
                .toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search).apply {
            lifecycleOwner = this@SearchActivity
        }
        setupSearchView()
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

    override fun onPause() {
        overridePendingTransition(0, 0)
        super.onPause()
        ImeUtil.hideIme(searchView)
    }

    override fun onEnterAnimationComplete() {
        if (focusQuery) {
            ImeUtil.showIme(searchView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        focusQuery = false
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun render(viewState: SearchUiModel) {
        loadingView.isVisible = viewState is LoadingState
        noItemsView.isVisible = viewState.hasNoItems()
        if (viewState is DoneState) {
            listAdapter.submitList(viewState.items)
        }
    }

    private fun setupSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
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
        searchBack.setOnClickListener { dismiss() }
    }

    private fun dismiss() {
        searchBack.background = null
        finishAfterTransition()
    }

    override fun onBackPressed() {
        dismiss()
    }
}
