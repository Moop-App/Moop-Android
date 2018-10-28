package soup.movie.ui.main.now

import soup.movie.ui.main.movie.MovieListContract
import soup.movie.ui.main.movie.MovieListFragment
import javax.inject.Inject

class NowFragment : MovieListFragment() {

    @Inject
    override lateinit var presenter: MovieListContract.Presenter

    companion object {

        fun newInstance(): NowFragment = NowFragment()
    }
}
