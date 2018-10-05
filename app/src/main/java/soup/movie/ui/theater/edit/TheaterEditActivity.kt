package soup.movie.ui.theater.edit

import android.content.Context
import android.view.View
import com.robinhood.ticker.TickerUtils
import kotlinx.android.synthetic.main.activity_theater_edit.*
import kotlinx.android.synthetic.main.activity_theater_edit_footer.*
import soup.movie.R
import soup.movie.databinding.ActivityTheaterEditBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditViewState.LoadingState
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import soup.movie.util.setVisibleIf
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
        tabLayout.setupWithViewPager(viewPager, true)
        pageAdapter = TheaterEditPageAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = pageAdapter.count
        viewPager.adapter = pageAdapter
        currentCountView.setCharacterLists(TickerUtils.provideNumberList())
    }

    override fun render(viewState: TheaterEditViewState) {
        printRenderLog { viewState }
        loadingView.setVisibleIf { viewState is LoadingState }
        currentCountView.text = viewState.getCurrentCount()
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked()
        finish()
    }
}
