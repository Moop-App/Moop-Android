package soup.movie.ui.home.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeContentsBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.home.*
import soup.movie.util.doOnApplyWindowInsets
import soup.movie.util.isInProgress
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

class HomePlanFragment : BaseFragment(), HomeTabFragment {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: HomeContentsBinding
    private val viewModel: HomePlanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeContentsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.init(viewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun HomeContentsBinding.adaptSystemWindowInset() {
        root.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            listView.updatePadding(
                bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            )
        }
    }

    override fun onDestroyView() {
        binding.listView.setOnTouchListener(null)
        super.onDestroyView()
    }

    private fun HomeContentsBinding.init(viewModel: HomePlanViewModel) {

        val listAdapter = HomeListAdapter { movie, sharedElements ->
            analytics.clickMovie()
            MovieSelectManager.select(movie)
            findNavController().navigate(
                HomeFragmentDirections.actionToDetail(),
                ActivityNavigatorExtras(
                    activityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                )
            )
        }
        listView.apply {
            adapter = listAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
            overScrollMode = View.OVER_SCROLL_NEVER
            setOnTouchListener(HomeListScrollEffect(this))
        }
        errorView.setOnDebounceClickListener {
            viewModel.refresh()
        }
        viewModel.contentsUiModel.observe(viewLifecycleOwner) {
            render(it)
            listAdapter.submitList(it.movies)
        }
    }

    private fun HomeContentsBinding.render(uiModel: HomeContentsUiModel) {
        loadingView.isInProgress = uiModel.isLoading
        errorView.isVisible = uiModel.isError
        noItemsView.isVisible = uiModel.hasNoItem
    }

    override fun scrollToTop() {
        binding.listView.smoothScrollToPosition(0)
    }
}
