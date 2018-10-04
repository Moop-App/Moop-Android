package soup.movie.ui.theater.edit.megabox

import soup.movie.ui.theater.edit.tab.TheaterEditChildContract
import soup.movie.ui.theater.edit.tab.TheaterEditChildFragment
import javax.inject.Inject

class MegaboxEditFragment : TheaterEditChildFragment() {

    @Inject
    override lateinit var presenter: TheaterEditChildContract.Presenter

    override val title: String = "메가박스"

    companion object {

        fun newInstance() = MegaboxEditFragment()
    }
}
