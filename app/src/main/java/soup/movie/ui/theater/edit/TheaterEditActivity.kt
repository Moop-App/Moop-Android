package soup.movie.ui.theater.edit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_theater_edit.*
import kotlinx.android.synthetic.main.activity_theater_edit_footer.*
import soup.movie.R
import soup.movie.data.helper.getChipLayout
import soup.movie.databinding.ActivityTheaterEditBinding
import soup.movie.ui.LegacyBaseActivity
import soup.movie.ui.theater.edit.TheaterEditContentViewState.LoadingState
import soup.movie.util.delegates.contentView
import soup.movie.util.inflate
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

class TheaterEditActivity :
    LegacyBaseActivity<TheaterEditContract.View, TheaterEditContract.Presenter>(),
    TheaterEditContract.View {

    private var pendingFinish: Boolean = false

    override val binding by contentView<TheaterEditActivity, ActivityTheaterEditBinding>(
        R.layout.activity_theater_edit
    )

    @Inject
    override lateinit var presenter: TheaterEditContract.Presenter

    private lateinit var pageAdapter: TheaterEditPageAdapter

    private val footerPanel by lazy {
        BottomSheetBehavior.from(footerView).apply {
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onSlide(v: View, offset: Float) = Unit

                override fun onStateChanged(v: View, state: Int) = tryToFinish()
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                selectedTheaterGroup?.run {
                    (0 until childCount)
                        .mapNotNull { getChildAt(it) }
                        .mapNotNull { it.findViewById<Chip>(R.id.theaterChip) }
                        .forEach { sharedElements[it.transitionName] = it }
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
        peekView.setOnDebounceClickListener {
            footerPanel.state = when (footerPanel.state) {
                STATE_COLLAPSED -> STATE_EXPANDED
                else -> STATE_COLLAPSED
            }
        }
    }

    private fun View.blockExtraTouchEvents() = setOnTouchListener { _, _ -> true }

    override fun render(viewState: TheaterEditContentViewState) {
        loadingView.isVisible = viewState is LoadingState
    }

    override fun render(viewState: TheaterEditFooterViewState) {
        val theaters = viewState.theaterList
        currentCountView.text = theaters.size.toString()
        confirmButton.setBackgroundResource(
            if (viewState.isFull()) {
                R.drawable.bg_button_confirm_full
            } else {
                R.drawable.bg_button_confirm
            }
        )
        noTheaterView.isVisible = theaters.isEmpty()
        selectedTheaterGroup.run {
            TransitionManager.beginDelayedTransition(this)
            removeAllViews()
            theaters.map {
                inflate<Chip>(context, it.getChipLayout()).apply {
                    text = it.name
                    transitionName = it.id
                    tag = it.id
                    setOnClickListener { _ -> presenter.remove(it) }
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
        footerView.postOnAnimationDelayed(350) {
            footerPanel.state = STATE_COLLAPSED
        }
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked()
        setResultAndFinish()
    }

    override fun onBackPressed() {
        setResultAndFinish()
    }

    private fun setResultAndFinish() {
        setResult(RESULT_OK)
        pendingFinish = true
        footerPanel.state = STATE_EXPANDED
        tryToFinish()
    }

    private fun tryToFinish() {
        if (footerPanel.state == STATE_EXPANDED && pendingFinish) {
            pendingFinish = false
            finishAfterTransition()
        }
    }
}
