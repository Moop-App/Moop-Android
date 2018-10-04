package soup.movie.ui.theater.edit.cgv

import io.reactivex.Observable
import soup.movie.data.TheaterEditManager
import soup.movie.data.model.AreaGroup
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.theater.edit.tab.TheaterEditChildPresenter

class CgvEditPresenter(private val manager: TheaterEditManager,
                       theatersSetting: TheatersSetting) :
        TheaterEditChildPresenter(manager, theatersSetting) {

    override fun getAllTheatersObservable(): Observable<List<AreaGroup>> =
            manager.asCgvObservable().map { it.list }
}
