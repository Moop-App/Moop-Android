package soup.movie.ui.theater.edit

import androidx.annotation.Keep
import soup.movie.data.model.Theater
import soup.movie.domain.theater.edit.TheaterEditManager

sealed class TheaterEditContentUiModel {

    @Keep
    object LoadingState : TheaterEditContentUiModel()

    @Keep
    object ErrorState : TheaterEditContentUiModel()

    @Keep
    object DoneState : TheaterEditContentUiModel()
}

@Keep
class TheaterEditFooterUiModel(
    val theaterList: List<Theater>
) {

    fun isFull(): Boolean = theaterList.size >= TheaterEditManager.MAX_ITEMS
}
