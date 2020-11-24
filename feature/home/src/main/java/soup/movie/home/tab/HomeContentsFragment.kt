package soup.movie.home.tab

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RoughAdapterDataObserver
import dev.chrisbanes.insetter.Insetter
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.home.HomeContentsListAdapter
import soup.movie.home.HomeFragmentDirections
import soup.movie.home.R
import soup.movie.home.databinding.HomeTabContentsBinding
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.util.autoCleared
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

abstract class HomeContentsFragment : HomeTabFragment(R.layout.home_tab_contents), OnBackPressedListener {

    @Inject
    lateinit var analytics: EventAnalytics

    private var binding: HomeTabContentsBinding by autoCleared {
        listView.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
    }
    protected abstract val viewModel: HomeContentsViewModel

    private val adapterDataObserver = object : RoughAdapterDataObserver() {

        override fun onItemRangeUpdatedRoughly() {
            getListView()?.scrollToTopInternal(force = true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeTabContentsBinding.bind(view).apply {
            adaptSystemWindowInset()
            initViewState(viewModel)
            listView.adapter?.registerAdapterDataObserver(adapterDataObserver)
        }
    }

    private fun HomeTabContentsBinding.adaptSystemWindowInset() {
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updatePadding(
                    bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
                )
            }
            .applyToView(listView)
    }

    private fun HomeTabContentsBinding.initViewState(viewModel: HomeContentsViewModel) {
        val listAdapter = HomeContentsListAdapter(root.context) { movie, sharedElements ->
            analytics.clickMovie()
            findNavController().navigate(
                HomeFragmentDirections.actionToDetail(movie),
                ActivityNavigatorExtras(
                    activityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                )
            )
        }
        listView.apply {
            setItemViewCacheSize(20)
            adapter = listAdapter
            itemAnimator = FadeInAnimator()
        }
        errorView.root.setOnDebounceClickListener {
            viewModel.refresh()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            loadingView.isInProgress = it
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            errorView.root.isVisible = it
        }
        viewModel.contentsUiModel.observe(viewLifecycleOwner) {
            noItemsView.isVisible = it.movies.isEmpty()
            listAdapter.submitList(it.movies)
        }
    }

    override fun scrollToTop() {
        getListView()?.scrollToTopInternal()
    }

    override fun onBackPressed(): Boolean {
        return getListView()?.scrollToTopInternal() ?: false
    }

    private fun getListView(): RecyclerView? {
        return runCatching { binding.listView }.getOrNull()
    }
}
