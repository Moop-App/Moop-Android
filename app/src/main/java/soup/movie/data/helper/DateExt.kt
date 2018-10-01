package soup.movie.data.helper

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAdjusters
import soup.movie.data.model.Date
import kotlin.math.max

fun today(): LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))

infix fun LocalDate.until(to: LocalDate): List<LocalDate> =
        between(this, to.plusDays(1))

fun LocalDate.until(to: LocalDate, atLeast: Long): List<LocalDate> =
        between(this, to.plusDays(1), atLeast)

private fun between(from: LocalDate, to: LocalDate): List<LocalDate> =
        from toDays ChronoUnit.DAYS.between(from, to)

private fun between(from: LocalDate, to: LocalDate, atLeast: Long): List<LocalDate> =
        from toDays max(ChronoUnit.DAYS.between(from, to), atLeast)

private infix fun LocalDate.toDays(days: Long) = (0L until days).map { plusDays(it) }

fun LocalDate.toWeek() = toDays(7)

private fun LocalDate.toWeekInMonth(): List<LocalDate> {
    return with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toDays(7)
}

fun Date.localDate(): LocalDate = LocalDate.of(year, month, day)
