package soup.movie.ui.home.tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
import androidx.core.view.updatePadding
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeContentsBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.home.HomeFragmentDirections
import soup.movie.ui.home.HomeListAdapter
import soup.movie.ui.home.HomeListScrollEffect
import soup.movie.ui.home.MovieSelectManager
import soup.movie.util.doOnApplyWindowInsets
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

abstract class HomeTabFragment : BaseFragment() {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: HomeContentsBinding
    protected abstract val viewModel: HomeTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeContentsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.initViewState(viewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun HomeContentsBinding.adaptSystemWindowInset() {
        root.doOnApplyWindowInsets { _, windowInsets, initialPadding ->
            listView.updatePadding(
                bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onDestroyView() {
        binding.listView.setOnTouchListener(null)
        super.onDestroyView()
    }

    private fun HomeContentsBinding.initViewState(viewModel: HomeTabViewModel) {
        val listAdapter =
            HomeListAdapter { movie, sharedElements ->
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
            itemAnimator = FadeInAnimator()
            overScrollMode = View.OVER_SCROLL_NEVER
            setOnTouchListener(HomeListScrollEffect(this))
        }
        errorView.setOnDebounceClickListener {
            viewModel.refresh()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            loadingView.isInProgress = it
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            errorView.isVisible = it
        }
        viewModel.contentsUiModel.observe(viewLifecycleOwner) {
            val isTopPosition = listView.canScrollVertically(-1).not()

            noItemsView.isVisible = it.movies.isEmpty()
            listAdapter.submitList(it.movies)

            if (it.movies.isNotEmpty() && isTopPosition) {
                listView.postOnAnimationDelayed(100) {
                    listView.smoothScrollToPosition(0)
                }
            }
        }
    }

    fun scrollToTop() {
        if (::binding.isInitialized) {
            binding.listView.scrollToPosition(0)
        }
    }
}
