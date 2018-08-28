package soup.movie.ui.main.now

import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_vertical_list.*
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

    private lateinit var listAdapter: MovieListAdapter

    override val layoutRes: Int
        get() = R.layout.fragment_vertical_list

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listAdapter = MovieListAdapter(activity!!)
        list_view.let {
            it.layoutManager = gridLayoutManager(ctx, 3)
            it.adapter = listAdapter
            it.itemAnimator = SlideInUpAnimator()
            it.itemAnimator.addDuration = 200
            it.itemAnimator.removeDuration = 200
        }
        swipe_refresh_layout.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun render(viewState: NowViewState) {
        Timber.d("render: %s", viewState)
        when (viewState) {
            is LoadingState -> renderLoadingState()
            is DoneState -> renderDoneState(viewState)
        }
    }

    private fun renderLoadingState() {
        swipe_refresh_layout.isRefreshing = true
        list_view.visibility = GONE
    }

    private fun renderDoneState(state: DoneState) {
        swipe_refresh_layout.isRefreshing = false
        list_view.visibility = VISIBLE
        listAdapter.submitList(state.movies)
    }

    companion object {

        @JvmStatic
        fun newInstance(): NowFragment {
            return NowFragment()
        }
    }
}
