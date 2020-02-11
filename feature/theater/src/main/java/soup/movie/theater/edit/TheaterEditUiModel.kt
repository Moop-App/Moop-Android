package soup.movie.theater.edit

import androidx.annotation.Keep
import soup.movie.model.Theater

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
