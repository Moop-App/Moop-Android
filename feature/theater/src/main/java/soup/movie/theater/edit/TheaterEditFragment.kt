package soup.movie.theater.edit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.setupWithViewPager2
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.ext.lazyFast
import soup.movie.model.Theater
import soup.movie.theater.R
import soup.movie.theater.databinding.TheaterEditFooterBinding
import soup.movie.theater.databinding.TheaterEditFragmentBinding
import soup.movie.theater.edit.TheaterEditContentUiModel.LoadingState
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.util.inflate
import soup.movie.util.setOnDebounceClickListener
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TheaterEditFragment : Fragment(), OnBackPressedListener {

    private var pendingFinish: Boolean = false

    private lateinit var binding: TheaterEditFragmentBinding

    private val viewModel: TheaterEditViewModel by activityViewModels()

    private val pageAdapter by lazyFast {
        TheaterEditPageAdapter(childFragmentManager, lifecycle)
    }

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(v: View, offset: Float) {}

        override fun onStateChanged(v: View, state: Int) {
            tryToFinish()
        }
    }

    private lateinit var footerPanel: BottomSheetBehavior<View>
    private var originPeekHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                binding.footer.selectedTheaterGroup.run {
                    (0 until childCount)
                        .mapNotNull { getChildAt(it) }
                        .mapNotNull { it.findViewById<Chip>(R.id.theaterChip) }
                        .forEach { sharedElements[it.transitionName] = it }
                }
                Timber.d("setEnterSharedElementCallback: ${sharedElements.keys}")
            }
        })
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                sharedElements.clear()
                names.forEach { name ->
                    binding.footer.selectedTheaterGroup.findViewWithTag<View>(name)?.let {
                        sharedElements[name] = it
                    }
                }
                Timber.d("setExitSharedElementCallback: ${sharedElements.keys}")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
        binding = TheaterEditFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.initViewState()
        binding.adaptSystemWindowInset()

        viewModel.contentUiModel.observe(viewLifecycleOwner) {
            binding.render(it)
        }
        viewModel.footerUiModel.observe(viewLifecycleOwner) {
            binding.footer.render(it)

            //FixMe: find a timing to call startPostponedEnterTransition()
            startPostponedEnterTransition()
        }
        return binding.root
    }

    override fun onDestroyView() {
        footerPanel.removeBottomSheetCallback(bottomSheetCallback)
        super.onDestroyView()
    }

    private fun TheaterEditFragmentBinding.initViewState() {
        contentView.setOnInterceptTouchListener { _, _ ->
            footerPanel.takeIf { it.state == STATE_EXPANDED }
                ?.run { state = STATE_COLLAPSED }
        }
        viewPager.offscreenPageLimit = pageAdapter.itemCount
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager2(viewPager, titleProvider = pageAdapter, autoRefresh = true)

        // Footer

        footerPanel = BottomSheetBehavior.from(footer.root).apply {
            addBottomSheetCallback(bottomSheetCallback)
        }

        originPeekHeight = footerPanel.peekHeight
        footer.root.setOnTouchListener { _, _ -> true }
        footer.peekView.setOnDebounceClickListener {
            footerPanel.state = when (footerPanel.state) {
                STATE_COLLAPSED -> STATE_EXPANDED
                else -> STATE_COLLAPSED
            }
        }

        footerPanel.state = STATE_EXPANDED
        footer.root.postOnAnimationDelayed(500) {
            footerPanel.state = STATE_COLLAPSED
        }
        footer.confirmButton.setOnDebounceClickListener {
            onConfirmClicked()
        }
    }

    private fun TheaterEditFragmentBinding.adaptSystemWindowInset() {
        root.doOnApplyWindowInsets { view, insets, initialState ->
            view.updatePadding(top = initialState.paddings.top + insets.systemWindowInsetTop)

            val bottomSystemInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                insets.systemGestureInsets.bottom
            } else {
                insets.systemWindowInsetBottom
            }
            footerPanel.setPeekHeight(bottomSystemInset + originPeekHeight)
        }
        viewPager.doOnApplyWindowInsets { viewPager, insets, initialState ->
            viewPager.updatePadding(
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
    }

    private fun TheaterEditFragmentBinding.render(viewState: TheaterEditContentUiModel) {
        loadingView.isVisible = viewState is LoadingState
    }

    private fun TheaterEditFooterBinding.render(uiModel: TheaterEditFooterUiModel) {
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
            theaters.forEach { theater ->
                val chip = inflate<Chip>(context, theater.getChipLayout()).apply {
                    text = theater.name
                    transitionName = theater.id
                    tag = theater.id
                    setOnClickListener { viewModel.remove(theater) }
                }
                addView(chip)
            }
        }
    }

    @LayoutRes
    private fun Theater.getChipLayout(): Int {
        return when(type) {
            Theater.TYPE_CGV -> R.layout.chip_action_cgv
            Theater.TYPE_LOTTE -> R.layout.chip_action_lotte
            Theater.TYPE_MEGABOX -> R.layout.chip_action_megabox
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }

    private fun onConfirmClicked() {
        viewModel.onConfirmClicked()
        setResultAndFinish()
        findNavController().navigateUp()
    }

    override fun onBackPressed(): Boolean {
        setResultAndFinish()
        return false
    }

    private fun setResultAndFinish() {
        pendingFinish = true
        footerPanel.state = STATE_EXPANDED
        tryToFinish()
    }

    private fun tryToFinish() {
        if (footerPanel.state == STATE_EXPANDED && pendingFinish) {
            pendingFinish = false
        }
    }
}
