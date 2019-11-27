package soup.movie.ui.home.tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linecorp.pasha.di.qualifier.NamedHome
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeContentsBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.home.*
import soup.movie.util.doOnApplyWindowInsets
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

abstract class HomeTabFragment : BaseFragment() {

    @Inject
    lateinit var analytics: EventAnalytics

    @Inject
    @field:NamedHome
    lateinit var viewPool: RecyclerView.RecycledViewPool

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
            itemAnimator = HomeTabItemAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
            overScrollMode = View.OVER_SCROLL_NEVER
            setOnTouchListener(HomeListScrollEffect(this))
            setRecycledViewPoolForMovie(viewPool)
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

    fun scrollToTop() {
        if (::binding.isInitialized) {
            binding.listView.scrollToPosition(0)
        }
    }

    fun RecyclerView.setRecycledViewPoolForMovie(pool: RecyclerView.RecycledViewPool) {
        setRecycledViewPool(pool)
        layoutManager?.run {
            if (this is LinearLayoutManager) {
                recycleChildrenOnDetach = true
            }
        }
    }
}
