package soup.movie.ui.detail

import android.net.Uri
import soup.movie.ui.LegacyBaseContract

interface DetailContract {

    interface Presenter : LegacyBaseContract.Presenter<View> {

        fun requestShareImage(url: String)
    }

    interface View : LegacyBaseContract.View {

        fun render(viewState: DetailViewState)

        fun doShareImage(imageUri: Uri, mimeType: String)
    }
}
