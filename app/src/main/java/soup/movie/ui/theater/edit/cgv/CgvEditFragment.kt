package soup.movie.ui.theater.edit.cgv

import soup.movie.ui.theater.edit.tab.TheaterEditChildContract
import soup.movie.ui.theater.edit.tab.TheaterEditChildFragment
import javax.inject.Inject

class CgvEditFragment : TheaterEditChildFragment() {

    @Inject
    override lateinit var presenter: TheaterEditChildContract.Presenter

    override val title: String = "CGV"

    companion object {

        fun newInstance() = CgvEditFragment()
    }
}
