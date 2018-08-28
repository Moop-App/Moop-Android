package soup.movie.ui.main.now

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import butterknife.BindView
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import soup.movie.R
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.MovieListAdapter
import soup.movie.ui.main.now.NowContract.Presenter
import soup.movie.ui.main.now.NowContract.View
import soup.movie.ui.main.now.NowViewState.DoneState
import soup.movie.ui.main.now.NowViewState.LoadingState
import soup.movie.util.RecyclerViewUtil.gridLayoutManager
import timber.log.Timber
import javax.inject.Inject

class NowFragment : BaseTabFragment<View, Presenter>(), View {

    @Inject
    override lateinit var presenter: NowContract.Presenter

    @BindView(R.id.swipe_layout)
    internal lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.list)
    internal lateinit var listView: RecyclerView

    private lateinit var listAdapter: MovieListAdapter

    override val layoutRes: Int
        get() = R.layout.fragment_vertical_list

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listAdapter = MovieListAdapter(activity!!)
        listView.layoutManager = gridLayoutManager(ctx, 3)
        listView.adapter = listAdapter
        listView.itemAnimator = SlideInUpAnimator()
        listView.itemAnimator.addDuration = 200
        listView.itemAnimator.removeDuration = 200
        swipeRefreshLayout.setOnRefreshListener { presenter.refresh() }
    }

    override fun render(viewState: NowViewState) {
        Timber.d("render: %s", viewState)
        when (viewState) {
            is LoadingState -> renderLoadingState()
            is DoneState -> renderDoneState(viewState)
        }
    }

    private fun renderLoadingState() {
        swipeRefreshLayout.isRefreshing = true
        listView.visibility = GONE
    }

    private fun renderDoneState(state: DoneState) {
        swipeRefreshLayout.isRefreshing = false
        listAdapter.submitList(state.movies)
        listView.visibility = VISIBLE
    }

    companion object {

        @JvmStatic
        fun newInstance(): NowFragment {
            return NowFragment()
        }
    }
}
