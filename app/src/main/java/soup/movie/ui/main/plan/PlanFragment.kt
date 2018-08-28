package soup.movie.ui.main.plan

import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_vertical_list.*
import soup.movie.R
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.MovieListAdapter
import soup.movie.ui.main.plan.PlanViewState.DoneState
import soup.movie.ui.main.plan.PlanViewState.LoadingState
import soup.movie.util.RecyclerViewUtil.gridLayoutManager
import timber.log.Timber
import javax.inject.Inject

class PlanFragment
    : BaseTabFragment<PlanContract.View, PlanContract.Presenter>(),
        PlanContract.View {

    @Inject
    override lateinit var presenter: PlanContract.Presenter

    private lateinit var listAdapter: MovieListAdapter

    override val layoutRes: Int
        get() = R.layout.fragment_vertical_list

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listAdapter = MovieListAdapter(activity!!)
        listView.let {
            it.layoutManager = gridLayoutManager(ctx, 3)
            it.adapter = listAdapter
            it.itemAnimator = SlideInUpAnimator()
            it.itemAnimator.addDuration = 200
            it.itemAnimator.removeDuration = 200
        }
        swipeRefreshLayout.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun render(viewState: PlanViewState) {
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

    private fun renderDoneState(viewState: DoneState) {
        swipeRefreshLayout.isRefreshing = false
        listView.visibility = VISIBLE
        listAdapter.submitList(viewState.movies)
    }

    companion object {

        @JvmStatic
        fun newInstance(): PlanFragment {
            return PlanFragment()
        }
    }
}
