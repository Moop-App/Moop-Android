package soup.movie.util.helper

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun today(): LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
fun yesterday(): LocalDate = today().minusDays(1)

/**
 * 매달 마지막 수요일은 "문화가 있는 날"
 * @link http://www.culture.go.kr/wday
 *
 * '문화의 날' 바로 직전의 '일/월/화/수'요일은 '문화의 날' 주간으로 정한다.
 */
fun LocalDate.isInWeekOfCultureDay(): Boolean {
    val lastDayOfMonth = LocalDate.of(year, month, month.maxLength())
    val cultureDayOfMonth = lastDayOfMonth.minusDaysTo(DayOfWeek.WEDNESDAY)
    val mondayOfCultureDay = cultureDayOfMonth.minusDaysTo(DayOfWeek.SUNDAY)
    return this in mondayOfCultureDay..cultureDayOfMonth
}

fun LocalDate.minusDaysTo(dayOfWeekToSubtract: DayOfWeek): LocalDate {
    return minusDays(dayOfWeek.calculateMinusDaysTo(dayOfWeekToSubtract))
}

fun DayOfWeek.calculateMinusDaysTo(dayOfWeekToPrevious: DayOfWeek): Long {
    var result = value - dayOfWeekToPrevious.value
    if (result < 0) {
        result += DayOfWeek.SUNDAY.value
    }
    return result.toLong()
}

fun LocalDate.MM_DD(): String {
    return format(DateTimeFormatter.ofPattern("MM.dd"))
}
