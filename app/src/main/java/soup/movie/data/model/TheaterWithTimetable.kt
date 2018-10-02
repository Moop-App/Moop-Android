package soup.movie.data.model

import androidx.annotation.Keep
import soup.widget.recyclerview.callback.HasId

@Keep
data class TheaterWithTimetable(
        val theater: Theater,
        val hallList: List<Hall> = emptyList(),
        val selected: Boolean = false) : HasId {

    override val id: String
        get() = theater.code
}
