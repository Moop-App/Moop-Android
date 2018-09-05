package soup.movie.data

import androidx.annotation.ColorRes
import soup.movie.R
import soup.movie.data.model.Movie
import soup.movie.util.TimeUtil

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

fun Movie.getRemainDayLabel(): String = when (getRemainDay()) {
    UNKNOWN_REMAIN_DAY -> ""
    else -> "D-$this"
}

fun Movie.isInOneWeek(): Boolean = getRemainDay() <= 7

private fun Movie.getRemainDay(): Int {
    val date = openDate.split(".")
    return when {
        date.size != 3 -> UNKNOWN_REMAIN_DAY
        else -> TimeUtil.remainDayTo(date[0].toInt(), date[1].toInt(), date[2].toInt())
    }
}

private const val UNKNOWN_REMAIN_DAY = 9999
