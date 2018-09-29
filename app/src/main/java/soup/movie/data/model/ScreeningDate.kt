package soup.movie.data.model

import androidx.annotation.Keep
import org.threeten.bp.LocalDate
import soup.widget.recyclerview.callback.HasId

@Keep
data class ScreeningDate(
        val date: LocalDate,
        val enabled: Boolean = true,
        val selected: Boolean = false): HasId {

    override val id: String
        get() = date.toEpochDay().toString()

    val day: String = date.dayOfMonth.toString()
}
