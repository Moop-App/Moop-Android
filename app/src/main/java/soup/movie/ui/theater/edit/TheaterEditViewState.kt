package soup.movie.ui.theater.edit

import androidx.annotation.Keep
import soup.movie.data.TheaterEditManager.Companion.MAX_ITEMS

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
            val selectedItemCount: Int) : TheaterEditViewState()

    fun getCurrentCount(): Int =
            when (this) {
                is DoneState -> selectedItemCount
                else -> 0
            }

    fun isFull(): Boolean = getCurrentCount() >= MAX_ITEMS
}
