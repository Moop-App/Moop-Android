package soup.movie.ui.theater.edit.lotte

import soup.movie.ui.theater.edit.tab.TheaterEditChildContract
import soup.movie.ui.theater.edit.tab.TheaterEditChildFragment
import javax.inject.Inject

class LotteEditFragment : TheaterEditChildFragment() {

    @Inject
    override lateinit var presenter: TheaterEditChildContract.Presenter

    override val title: String = "롯데시네마"

    companion object {

        fun newInstance() = LotteEditFragment()
    }
}
