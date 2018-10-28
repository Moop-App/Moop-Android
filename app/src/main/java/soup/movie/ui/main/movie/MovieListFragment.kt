package soup.movie.ui.main.movie

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_vertical_list.*
import soup.movie.R
import soup.movie.data.MovieSelectManager
import soup.movie.databinding.FragmentVerticalListBinding
import soup.movie.theme.util.getColorAttr
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.helper.EventAnalytics
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.movie.MovieListViewState.*
import soup.movie.ui.main.movie.filter.MovieFilterFragment
import soup.movie.util.log.printRenderLog
import soup.movie.util.setVisibleIf
import javax.inject.Inject

abstract class MovieListFragment :
        BaseTabFragment<MovieListContract.View, MovieListContract.Presenter>(),
        MovieListContract.View, BaseTabFragment.OnReselectListener {

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazy {
        MovieListAdapter { index, movie, sharedElements ->
            analytics.clickItem(index, movie)
            MovieSelectManager.select(movie)
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            startActivityForResult(intent, 0, ActivityOptions
                    .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                    .toBundle())
        }
    }

    override val menuResource: Int?
        get() = R.menu.fragment_movie_list

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            if (panelIsShown()) {
                hidePanel()
            } else {
                analytics.clickMenuFilter()
                showPanel(MovieFilterFragment.toPanelData())
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapSharedElements(names: List<String>,
                                     sharedElements: MutableMap<String, View>) {
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

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentVerticalListBinding.inflate(inflater, container, false).root

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
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
                presenter.refresh()
            }
        }
        errorView.setOnClickListener {
            presenter.refresh()
        }
    }

    override fun render(viewState: MovieListViewState) {
        printRenderLog { viewState }
        swipeRefreshLayout?.isRefreshing = viewState is LoadingState
        errorView?.setVisibleIf { viewState is ErrorState }
        if (viewState is DoneState) {
            listAdapter.submitList(viewState.movies)
        }
    }

    override fun onReselect() {
        listView?.smoothScrollToPosition(0)
    }
}
