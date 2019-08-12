package soup.movie.ui.map

import androidx.annotation.Keep
import soup.movie.data.model.Theater

sealed class TheaterMapUiModel {

    @Keep
    object LoadingState : TheaterMapUiModel() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : TheaterMapUiModel() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(val myTheaters: List<Theater>) : TheaterMapUiModel()
}
