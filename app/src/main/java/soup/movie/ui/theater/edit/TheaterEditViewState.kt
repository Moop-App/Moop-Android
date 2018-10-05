package soup.movie.ui.theater.edit

import androidx.annotation.Keep
import soup.movie.data.TheaterEditManager.Companion.MAX_ITEMS
import soup.movie.data.model.Theater

sealed class TheaterEditViewState {

    @Keep
    object LoadingState : TheaterEditViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : TheaterEditViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(
            internal val theaterList: List<Theater>) : TheaterEditViewState()

    fun getTheaterList(): List<Theater> =
            if (this is DoneState) theaterList else emptyList()

    fun getCurrentCount(): Int = getTheaterList().size

    fun isFull(): Boolean = getCurrentCount() >= MAX_ITEMS
}
