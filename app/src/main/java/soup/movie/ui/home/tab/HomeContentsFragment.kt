package soup.movie.ui.home.tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.observe
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RoughAdapterDataObserver
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeTabContentsBinding
import soup.movie.model.Movie
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.home.HomeFragmentDirections
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

abstract class HomeContentsFragment : HomeTabFragment(), OnBackPressedListener {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: HomeTabContentsBinding
    protected abstract val viewModel: HomeContentsViewModel

    private val adapterDataObserver = object : RoughAdapterDataObserver() {

        override fun onItemRangeUpdatedRoughly() {
            getListView()?.scrollToTopInternal(force = true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeTabContentsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adaptSystemWindowInset()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViewState(viewModel)
        binding.listView.adapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun HomeTabContentsBinding.adaptSystemWindowInset() {
        listView.doOnApplyWindowInsets { _, insets, initialState ->
            listView.updatePadding(
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onDestroyView() {
        binding.listView.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        binding.listView.setOnTouchListener(null)
        super.onDestroyView()
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
            noItemsView.isVisible = it.movies.isEmpty()
            onUpdateList(listView, it.movies)
        }
    }

    protected open fun onUpdateList(listView: RecyclerView, movies: List<Movie>) {
        val listAdapter = listView.adapter
        if (listAdapter is HomeContentsListAdapter) {
            listAdapter.submitList(movies)
        }
    }

    override fun scrollToTop() {
        getListView()?.scrollToTopInternal()
    }

    override fun onBackPressed(): Boolean {
        return getListView()?.scrollToTopInternal() ?: false
    }

    private fun getListView(): RecyclerView? {
        return if (::binding.isInitialized) binding.listView else null
    }
}
