package soup.movie.ui.theater.edit

import android.app.SharedElementCallback
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.postOnAnimationDelayed
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.chip.Chip
import com.robinhood.ticker.TickerUtils
import kotlinx.android.synthetic.main.activity_theater_edit.*
import kotlinx.android.synthetic.main.activity_theater_edit_footer.*
import soup.movie.R
import soup.movie.data.helper.getChipLayout
import soup.movie.databinding.ActivityTheaterEditBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditContentViewState.LoadingState
import soup.movie.util.blockExtraTouchEvents
import soup.movie.util.delegates.contentView
import soup.movie.util.inflate
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

    private val footerPanel by lazy {
        BottomSheetBehavior.from(footerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                names.forEach {
                    selectedTheaterGroup.findViewWithTag<View>(it)?.run {
                        sharedElements[it] = this
                    }
                }
            }
        })
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        contentView.setOnInterceptTouchListener { _, _ ->
            footerPanel.takeIf { it.state == STATE_EXPANDED }
                    ?.run { state = STATE_COLLAPSED }
        }
        tabLayout.setupWithViewPager(viewPager, true)
        pageAdapter = TheaterEditPageAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = pageAdapter.count
        viewPager.adapter = pageAdapter
        footerView.blockExtraTouchEvents()
        footerPanel.state = STATE_EXPANDED
        peekView.setOnClickListener {
            footerPanel.state = when (footerPanel.state) {
                STATE_COLLAPSED -> STATE_EXPANDED
                else -> STATE_COLLAPSED
            }
        }
        currentCountView.setCharacterLists(TickerUtils.provideNumberList())
    }

    override fun render(viewState: TheaterEditContentViewState) {
        printRenderLog { viewState }
        loadingView.setVisibleIf { viewState is LoadingState }
    }

    override fun render(viewState: TheaterEditFooterViewState) {
        printRenderLog { viewState }
        val theaters = viewState.theaterList
        currentCountView.text = theaters.size.toString()
        confirmButton.setBackgroundResource(
                if (viewState.isFull()) {
                    R.drawable.bg_button_confirm_full
                } else {
                    R.drawable.bg_button_confirm
                }
        )
        noTheaterView.setVisibleIf { theaters.isEmpty() }
        selectedTheaterGroup.run {
            TransitionManager.beginDelayedTransition(this)
            removeAllViews()
            theaters.map {
                inflate<Chip>(context, it.getChipLayout()).apply {
                    text = it.name
                    transitionName = it.code
                    tag = it.code
                }
            }.forEach { addView(it) }
        }

        //FixMe: find a timing to call startPostponedEnterTransition()
        selectedTheaterGroup.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        footerView.postOnAnimationDelayed(300) {
            footerPanel.state = STATE_COLLAPSED
        }
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked()
        onBackPressed()
    }

    override fun onBackPressed() {
        footerPanel.state = STATE_EXPANDED
        footerView.postOnAnimationDelayed(80) {
            super.onBackPressed()
        }
    }
}
