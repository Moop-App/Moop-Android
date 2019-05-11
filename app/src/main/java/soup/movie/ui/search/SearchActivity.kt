package soup.movie.ui.search

import android.app.ActivityOptions
import android.app.SearchManager
import android.content.Context
import android.content.Intent
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
import soup.movie.ui.LegacyBaseActivity
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.main.movie.MovieListAdapter
import soup.movie.ui.search.SearchViewState.DoneState
import soup.movie.ui.search.SearchViewState.LoadingState
import soup.movie.util.ImeUtil
import javax.inject.Inject

class SearchActivity :
    LegacyBaseActivity<SearchContract.View, SearchContract.Presenter>(),
    SearchContract.View {

    @Inject
    override lateinit var presenter: SearchContract.Presenter

    @Inject
    lateinit var analytics: EventAnalytics

    private var focusQuery = true

    private val listAdapter by lazy {
        MovieListAdapter { movie, sharedElements ->
            analytics.clickMovie(isNow = movie.isNow)
            MovieSelectManager.select(movie)
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, 0, ActivityOptions
                .makeSceneTransitionAnimation(this, *sharedElements)
                .toBundle())
        }
    }

    override fun setupContentView() {
        DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search).apply {
            lifecycleOwner = this@SearchActivity
        }
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        setupSearchView()
        listView.apply {
            adapter = listAdapter
            itemAnimator = FadeInAnimator().apply {
                addDuration = 0
                removeDuration = 0
            }
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
    }

    override fun render(viewState: SearchViewState) {
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
                searchFor(query)
                return true
            }
        })
        searchBack.setOnClickListener { dismiss() }
    }

    fun searchFor(query: String) {
        presenter.searchFor(query)
    }

    private fun dismiss() {
        searchBack.background = null
        finishAfterTransition()
    }

    override fun onBackPressed() {
        dismiss()
    }
}
