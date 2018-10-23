package soup.movie.ui.theater.edit.megabox

import io.reactivex.Observable
import soup.movie.data.TheaterEditManager
import soup.movie.data.model.AreaGroup
import soup.movie.ui.theater.edit.tab.TheaterEditChildPresenter

class MegaboxEditPresenter(private val manager: TheaterEditManager) :
        TheaterEditChildPresenter(manager) {

    override fun getAllTheatersObservable(): Observable<List<AreaGroup>> =
            manager.asMegaboxObservable().map { it.list }
}
