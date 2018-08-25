package soup.movie.ui.detail

import soup.movie.data.model.Movie
import soup.movie.ui.BaseContract

interface DetailContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun requestData(movie: Movie)
    }

    interface View : BaseContract.View {

        fun render(viewState: DetailViewState)
    }
}
