package soup.movie.ui.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeTabFavoriteBinding
import soup.movie.ui.home.HomeFragmentDirections
import soup.movie.ui.home.HomeListAdapter
import soup.movie.ui.home.MovieSelectManager
import soup.movie.ui.home.tab.HomeTabFragment
import javax.inject.Inject

class HomeFavoriteFragment : HomeTabFragment() {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: HomeTabFavoriteBinding
    private val viewModel: HomeFavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeTabFavoriteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.initViewState(viewModel)
        return binding.root
    }

    private fun HomeTabFavoriteBinding.initViewState(viewModel: HomeFavoriteViewModel) {
        val listAdapter = HomeListAdapter(root.context) { movie, sharedElements ->
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
        return if (::binding.isInitialized) binding.listView else null
    }
}
