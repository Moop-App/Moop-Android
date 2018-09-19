package soup.movie.ui.main.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_vertical_list.*
import soup.movie.databinding.FragmentVerticalListBinding
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.plan.PlanViewState.*
import soup.movie.util.log.printRenderLog
import soup.movie.util.setGoneIf
import soup.movie.util.setVisibleIf
import javax.inject.Inject

class PlanFragment :
        BaseTabFragment<PlanContract.View, PlanContract.Presenter>(),
        PlanContract.View, BaseTabFragment.OnReselectListener {

    @Inject
    override lateinit var presenter: PlanContract.Presenter

    private val listAdapter by lazy {
        PlanListAdapter(requireActivity())
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
        errorView.setOnClickListener {
            presenter.refresh()
        }
    }

    override fun render(viewState: PlanViewState) {
        printRenderLog { viewState }
        swipeRefreshLayout?.isRefreshing = viewState is LoadingState
        errorView?.setVisibleIf { viewState is ErrorState }
        listView?.setGoneIf { viewState is LoadingState }
        if (viewState is DoneState) {
            listAdapter.submitList(viewState.movies)
        }
    }

    override fun onReselect() {
        listView?.smoothScrollToPosition(0)
    }

    companion object {

        fun newInstance(): PlanFragment = PlanFragment()
    }
}
