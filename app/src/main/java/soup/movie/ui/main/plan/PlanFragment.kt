package soup.movie.ui.main.plan

import soup.movie.ui.main.movie.MovieListContract
import soup.movie.ui.main.movie.MovieListFragment
import javax.inject.Inject

class PlanFragment : MovieListFragment() {

    @Inject
    override lateinit var presenter: MovieListContract.Presenter

    companion object {

        fun newInstance(): PlanFragment = PlanFragment()
    }
}
