package soup.movie.ui.theater.edit

import androidx.annotation.Keep

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

    fun getCurrentCount(): String =
            when (this) {
                is DoneState -> selectedItemCount
                else -> 0
            }.toString()
}
