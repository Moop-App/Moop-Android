package soup.movie.ui.main

import soup.movie.data.model.MovieId
import soup.movie.settings.impl.LastMainTabSetting
import soup.movie.ui.LegacyBaseContract

interface MainContract {

    interface Presenter : LegacyBaseContract.Presenter<View> {

        fun setCurrentTab(mode: LastMainTabSetting.Tab)

        fun requestMovie(movieId: MovieId?)
    }

    interface View : LegacyBaseContract.View {

        fun render(viewState: MainViewState)

        fun execute(action: MainActionState)
    }
}
