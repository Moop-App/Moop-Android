package soup.movie.ui.home.plan

import androidx.core.view.postOnAnimationDelayed
import androidx.recyclerview.widget.RecyclerView
import soup.movie.data.model.Movie
import soup.movie.ui.home.tab.HomeContentsTabFragment

class HomePlanFragment : HomeContentsTabFragment() {

    override val viewModel: HomePlanViewModel by viewModels()

    override fun onUpdateList(listView: RecyclerView, movies: List<Movie>) {
        listView.postOnAnimationDelayed(300) {
            super.onUpdateList(listView, movies)
        }
    }
}
