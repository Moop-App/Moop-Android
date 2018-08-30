package soup.movie.data

import androidx.annotation.ColorRes
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
