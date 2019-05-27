package soup.movie.ui.main.now

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_vertical_list.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.databinding.FragmentVerticalListBinding
import soup.movie.ui.base.OnReselectListener
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.movie.MovieListAdapter
import soup.movie.ui.main.movie.MovieListUiModel
import soup.movie.ui.main.movie.filter.MovieFilterFragment
import soup.movie.ui.search.SearchActivity
import soup.movie.util.getColorAttr
import soup.movie.util.observe
import javax.inject.Inject

class NowFragment : BaseTabFragment(), OnReselectListener {

    private val viewModel: NowViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazy {
        MovieListAdapter { movie, sharedElements ->
            analytics.clickMovie(isNow = movie.isNow)
            MovieSelectManager.select(movie)
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            startActivityForResult(intent, 0, ActivityOptions
                .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                .toBundle())
        }
    }

    override val menuResource: Int = R.menu.fragment_movie_list

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(requireActivity(), SearchActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_filter -> {
                if (panelIsShown()) {
                    hidePanel()
                } else {
                    analytics.clickMenuFilter()
                    showPanel(MovieFilterFragment.toPanelData())
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapSharedElements(
        names: List<String>,
        sharedElements: MutableMap<String, View>
    ) {
        sharedElements.clear()
        MovieSelectManager.getSelectedItem()?.run {
            listView.findViewWithTag<View>(id)?.let { movieView ->
                names.forEach { name ->
                    val id: Int = when (name) {
                        "background" -> R.id.backgroundView
                        "poster" -> R.id.posterView
                        "age_bg" -> R.id.ageBgView
                        "new" -> R.id.newView
                        "best" -> R.id.bestView
                        "d_day" -> R.id.dDayView
                        else -> View.NO_ID
                    }
                    movieView.findViewById<View>(id)?.let {
                        sharedElements[name] = it
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentVerticalListBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewState(view.context)
        viewModel.uiModel.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initViewState(ctx: Context) {
        listView.apply {
            adapter = listAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
        swipeRefreshLayout.apply {
            setProgressBackgroundColorSchemeColor(ctx.getColorAttr(R.attr.moop_stageColor))
            setColorSchemeColors(ctx.getColorAttr(R.attr.moop_stageStarColor))
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
        errorView.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun render(viewState: MovieListUiModel) {
        swipeRefreshLayout?.isRefreshing = viewState is MovieListUiModel.LoadingState
        errorView?.isVisible = viewState is MovieListUiModel.ErrorState
        noItemsView?.isVisible = viewState.hasNoItems()
        if (viewState is MovieListUiModel.DoneState) {
            listAdapter.submitList(viewState.movies)
        }
    }

    override fun onReselect() {
        listView?.smoothScrollToPosition(0)
    }

    companion object {

        fun newInstance(): NowFragment = NowFragment()
    }
}
