package soup.movie.ui.main.plan

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
import soup.movie.ui.main.plan.PlanViewState.DoneState
import soup.movie.ui.main.plan.PlanViewState.LoadingState
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class PlanFragment
    : BaseTabFragment<PlanContract.View, PlanContract.Presenter>(),
        PlanContract.View {

    @Inject
    override lateinit var presenter: PlanContract.Presenter

    private val listAdapter by lazy {
        PlanListAdapter(activity!!)
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

    override fun render(viewState: PlanViewState) {
        viewState.printRenderLog()
        when (viewState) {
            is LoadingState -> {
                swipeRefreshLayout?.isRefreshing = true
                listView?.visibility = GONE
            }
            is DoneState -> {
                swipeRefreshLayout?.isRefreshing = false
                listView?.visibility = VISIBLE
                listAdapter.submitList(viewState.movies)
            }
        }
    }

    companion object {

        fun newInstance(): PlanFragment = PlanFragment()
    }
}
