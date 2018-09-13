package soup.movie.ui.theater.edit

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.activity_theater_edit.*
import soup.movie.R
import soup.movie.databinding.ActivityTheaterEditBinding
import soup.movie.ui.BaseActivity
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class TheaterEditActivity :
        BaseActivity<TheaterEditContract.View, TheaterEditContract.Presenter>(),
        TheaterEditContract.View {

    override val binding by contentView<TheaterEditActivity, ActivityTheaterEditBinding>(
            R.layout.activity_theater_edit
    )

    @Inject
    override lateinit var presenter: TheaterEditContract.Presenter

    private lateinit var listAdapter: TheaterEditListAdapter

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listAdapter = TheaterEditListAdapter()
        listView.adapter = listAdapter
    }

    override fun render(viewState: TheaterEditViewState) {
        viewState.printRenderLog()
        listAdapter.submitList(viewState.areaGroups, viewState.selectedTheaters)
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked(listAdapter.getSelectedTheaters())
        finish()
    }
}
