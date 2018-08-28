package soup.movie.ui.theater.edit

import android.content.Context
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_theater_edit.*
import soup.movie.R
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditContract.Presenter
import soup.movie.ui.theater.edit.TheaterEditContract.View
import soup.movie.util.RecyclerViewUtil.verticalLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

class TheaterEditActivity : BaseActivity<View, Presenter>(), View {

    @Inject
    override lateinit var presenter: Presenter

    private lateinit var listAdapter: TheaterEditListAdapter

    override val layoutRes: Int
        get() = R.layout.activity_theater_edit

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        list_view.layoutManager = verticalLinearLayoutManager(this)
    }

    override fun render(viewState: TheaterEditViewState) {
        Timber.d("render: %s", viewState)
        listAdapter = TheaterEditListAdapter(
                viewState.allTheaters,
                viewState.selectedTheaters)
        list_view.adapter = listAdapter
    }

    @OnClick(R.id.button_cancel)
    fun onCancelClicked() {
        finish()
    }

    @OnClick(R.id.button_confirm)
    fun onConfirmClicked() {
        presenter.onConfirmClicked(listAdapter.selectedTheaters)
        finish()
    }
}
