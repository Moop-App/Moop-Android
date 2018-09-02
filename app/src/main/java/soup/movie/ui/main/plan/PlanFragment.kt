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
        listView.apply {
            adapter = listAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun render(viewState: PlanViewState) {
        Timber.d("render: %s", viewState)
        when (viewState) {
            is LoadingState -> {
                swipeRefreshLayout.isRefreshing = true
                listView.visibility = GONE
            }
            is DoneState -> {
                swipeRefreshLayout.isRefreshing = false
                listView.visibility = VISIBLE
                listAdapter.submitList(viewState.movies)
            }
        }
    }

    companion object {

        fun newInstance(): PlanFragment = PlanFragment()
    }
}
