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

    private lateinit var pageAdapter: TheaterEditPageAdapter

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        pageAdapter = TheaterEditPageAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = pageAdapter.count
        viewPager.adapter = pageAdapter
    }

    override fun render(viewState: TheaterEditViewState) {
        printRenderLog { viewState }
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked()
        finish()
    }
}
