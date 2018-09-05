package soup.movie.util

import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZoneId

object TimeUtil {

    private fun today(): LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))

    fun remainDayTo(endDate: LocalDate) : Int =
            Period.between(today(), endDate).days

    fun remainDayTo(year: Int, month: Int, dayOfMonth: Int) : Int =
            Period.between(today(), LocalDate.of(year, month, dayOfMonth)).days
}
