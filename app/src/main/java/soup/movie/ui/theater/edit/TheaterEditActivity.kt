package soup.movie.ui.theater.edit

import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
import androidx.databinding.DataBindingUtil
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
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditContentUiModel.LoadingState
import soup.movie.util.inflate
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener

class TheaterEditActivity : BaseActivity() {

    private var pendingFinish: Boolean = false

    private val viewModel: TheaterEditViewModel by viewModel()

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
        setupContentView()
        initViewState()
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
        viewModel.contentUiModel.observe(this) {
            render(it)
        }
        viewModel.footerUiModel.observe(this) {
            render(it)
        }
    }

    private fun setupContentView() {
        DataBindingUtil.setContentView<ActivityTheaterEditBinding>(this, R.layout.activity_theater_edit).apply {
            lifecycleOwner = this@TheaterEditActivity
        }
    }

    private fun initViewState() {
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

    private fun render(viewState: TheaterEditContentUiModel) {
        loadingView.isVisible = viewState is LoadingState
    }

    private fun render(uiModel: TheaterEditFooterUiModel) {
        val theaters = uiModel.theaterList
        currentCountView.text = theaters.size.toString()
        confirmButton.setBackgroundResource(
            if (uiModel.isFull()) {
                R.drawable.bg_button_confirm_full
            } else {
                R.drawable.bg_button_confirm
            }
        )
        noTheaterView.isVisible = theaters.isEmpty()
        selectedTheaterGroup.run {
            TransitionManager.beginDelayedTransition(this)
            removeAllViews()
            theaters.map { theater ->
                inflate<Chip>(context, theater.getChipLayout()).apply {
                    text = theater.name
                    transitionName = theater.id
                    tag = theater.id
                    setOnClickListener { viewModel.remove(theater) }
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
        viewModel.onConfirmClicked()
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
