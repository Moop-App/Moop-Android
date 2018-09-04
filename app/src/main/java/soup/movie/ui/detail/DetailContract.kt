package soup.movie.ui.detail

import android.net.Uri
import soup.movie.data.model.Movie
import soup.movie.ui.BaseContract

interface DetailContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun requestData(movie: Movie)

        fun requestShareImage(url: String)

        fun usePaletteTheme(): Boolean
    }

    interface View : BaseContract.View {

        fun render(viewState: DetailViewState)

        fun doShareImage(imageUri: Uri, mimeType: String)
    }
}
