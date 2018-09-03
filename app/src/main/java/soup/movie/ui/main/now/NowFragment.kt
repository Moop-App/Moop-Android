package soup.movie.ui.main.now

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_vertical_list.*
import soup.movie.databinding.FragmentVerticalListBinding
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.MovieListAdapter
import soup.movie.ui.main.now.NowViewState.DoneState
import soup.movie.ui.main.now.NowViewState.LoadingState
import timber.log.Timber
import javax.inject.Inject

class NowFragment : BaseTabFragment<NowContract.View, NowContract.Presenter>(), NowContract.View {

    @Inject
    override lateinit var presenter: NowContract.Presenter

    private val listAdapter by lazy {
        MovieListAdapter(activity!!)
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
        swipeRefreshLayout.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun render(viewState: NowViewState) {
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

        fun newInstance(): NowFragment = NowFragment()
    }
}
