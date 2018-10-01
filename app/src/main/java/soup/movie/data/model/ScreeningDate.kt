package soup.movie.data.model

import androidx.annotation.Keep
import org.threeten.bp.DayOfWeek
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

    val dayOfTheWeek: String = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
        null -> ""
    }
}
