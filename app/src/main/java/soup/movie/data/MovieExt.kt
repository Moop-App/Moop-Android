package soup.movie.data

import androidx.annotation.ColorRes
import org.threeten.bp.LocalDate
import org.threeten.bp.Period.between
import org.threeten.bp.ZoneId
import soup.movie.R
import soup.movie.data.model.Movie

@ColorRes
fun Movie.getColorAsAge(): Int = when (age) {
    "전체 관람가" -> R.color.green
    "12세 관람가" -> R.color.blue
    "15세 관람가" -> R.color.amber
    "청소년관람불가" -> R.color.red
    else -> R.color.grey
}

fun Movie.getSimpleAgeLabel(): String = when (age) {
    "전체 관람가" -> "전체"
    "12세 관람가" -> "12"
    "15세 관람가" -> "15"
    "청소년관람불가" -> "청불"
    else -> "미정"
}

fun Movie.getRemainDayLabel(): String = openDate()?.let {
    val dDay = between(today(), it).days
    return when (dDay) {
        0 -> "NEW"
        in 1..7 -> "D-$dDay"
        else -> ""
    }
} ?: ""

fun Movie.isInThePastWeek(): Boolean = isIn(-7..0)

fun Movie.isInTheNextWeek(): Boolean = isIn(0..7)

fun Movie.isIn(dayRange: IntRange): Boolean = openDate()?.let {
    between(today(), it)
            .run { (years == 0) and (months == 0) and (days in dayRange) }
} ?: false

private fun today(): LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))

private fun Movie.openDate(): LocalDate? = openDate.split(".").let {
    return if (it.size == 3) {
        LocalDate.of(it[0].toInt(), it[1].toInt(), it[2].toInt())
    } else {
        null
    }
}
