package soup.movie.data.helper

import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import soup.movie.data.model.Date

fun today(): LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))

fun Date.localDate(): LocalDate {
    return LocalDate.of(year.toInt(), month.toInt(), day.toInt())
}
