package soup.movie.home.plan

import androidx.core.view.postOnAnimationDelayed
import androidx.recyclerview.widget.RecyclerView
import soup.movie.ext.assistedViewModels
import soup.movie.home.tab.HomeContentsFragment
import soup.movie.model.Movie
import javax.inject.Inject

class HomePlanFragment : HomeContentsFragment() {

    @Inject
    lateinit var viewModelFactory: HomePlanViewModel.Factory
    override val viewModel: HomePlanViewModel by assistedViewModels {
        viewModelFactory.create()
    }

    override fun onUpdateList(listView: RecyclerView, movies: List<Movie>) {
        listView.postOnAnimationDelayed(300) {
            super.onUpdateList(listView, movies)
        }
    }
}
