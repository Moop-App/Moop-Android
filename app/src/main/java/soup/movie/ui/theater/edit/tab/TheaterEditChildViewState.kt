package soup.movie.ui.theater.edit.tab

import androidx.annotation.Keep
import soup.movie.data.model.AreaGroup

sealed class TheaterEditChildViewState {

    @Keep
    object LoadingState : TheaterEditChildViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : TheaterEditChildViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(
            val areaGroupList: List<AreaGroup>,
            val selectedTheaterIdSet: Set<String>) : TheaterEditChildViewState()
}
