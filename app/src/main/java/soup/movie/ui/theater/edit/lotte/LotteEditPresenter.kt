package soup.movie.ui.theater.edit.lotte

import io.reactivex.Observable
import soup.movie.data.TheaterEditManager
import soup.movie.data.model.AreaGroup
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.theater.edit.tab.TheaterEditChildPresenter

class LotteEditPresenter(private val manager: TheaterEditManager,
                         theatersSetting: TheatersSetting) :
        TheaterEditChildPresenter(manager, theatersSetting) {

    override fun getAllTheatersObservable(): Observable<List<AreaGroup>> =
            manager.asLotteObservable().map { it.list }
}
