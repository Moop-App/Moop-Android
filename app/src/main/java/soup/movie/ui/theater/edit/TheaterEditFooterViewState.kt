package soup.movie.ui.theater.edit

import androidx.annotation.Keep
import soup.movie.data.TheaterEditManager.Companion.MAX_ITEMS
import soup.movie.data.model.Theater

@Keep
data class TheaterEditFooterViewState(
        val theaterList: List<Theater>) {

    fun isFull(): Boolean = theaterList.size >= MAX_ITEMS
}
