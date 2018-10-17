package soup.movie.ui.theater.edit.tab

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_vertical_list.*
import soup.movie.databinding.FragmentTheaterEditBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.theater.edit.tab.TheaterEditChildViewState.DoneState
import soup.movie.util.log.printRenderLog

abstract class TheaterEditChildFragment :
        BaseFragment<TheaterEditChildContract.View, TheaterEditChildContract.Presenter>(),
        TheaterEditChildContract.View {

    private lateinit var listAdapter: TheaterEditChildListAdapter

    abstract val title: String

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentTheaterEditBinding.inflate(inflater, container, false).root

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listAdapter = TheaterEditChildListAdapter(presenter)
        listView.adapter = listAdapter
    }

    override fun render(viewState: TheaterEditChildViewState) {
        printRenderLog { viewState }
        if (viewState is DoneState) {
            TransitionManager.beginDelayedTransition(listView)
            listAdapter.submitList(viewState.areaGroupList, viewState.selectedTheaterIdSet)
        }
    }
}
