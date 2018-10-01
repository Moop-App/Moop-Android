package soup.movie.data.helper

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.TemporalAdjusters
import soup.movie.data.model.Date

fun today(): LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))

fun LocalDate.toDays(days: Long): List<LocalDate> {
    return (0L until days).map { plusDays(it) }
}

fun LocalDate.toWeek(): List<LocalDate> {
    return toDays(7)
}

fun LocalDate.toWeekInMonth(): List<LocalDate> {
    return with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toDays(7)
}

fun Date.localDate(): LocalDate {
    return LocalDate.of(year.toInt(), month.toInt(), day.toInt())
}
