package soup.movie.ui.theater.edit

import soup.movie.ui.theater.edit.TheaterEditContract.Presenter
import soup.movie.ui.theater.edit.TheaterEditContract.View

import android.content.Context
import android.support.v7.widget.RecyclerView

import javax.inject.Inject

import butterknife.BindView
import butterknife.OnClick
import soup.movie.R
import soup.movie.ui.BaseActivity

import soup.movie.util.RecyclerViewUtil.verticalLinearLayoutManager

class TheaterEditActivity : BaseActivity<View, Presenter>(), View {

    @Inject
    override lateinit var presenter: Presenter

    @BindView(R.id.list)
    lateinit var listView: RecyclerView

    private var listAdapter: TheaterEditListAdapter? = null

    override val layoutRes: Int
        get() = R.layout.activity_theater_edit

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listView.layoutManager = verticalLinearLayoutManager(this)
    }

    override fun render(viewState: TheaterEditViewState) {
        listAdapter = TheaterEditListAdapter(
                viewState.allTheaters,
                viewState.selectedTheaters)
        listView.adapter = listAdapter
    }

    @OnClick(R.id.button_cancel)
    fun onCancelClicked() {
        finish()
    }

    @OnClick(R.id.button_confirm)
    fun onConfirmClicked() {
        presenter.onConfirmClicked(listAdapter!!.selectedTheaters)
        finish()
    }
}
