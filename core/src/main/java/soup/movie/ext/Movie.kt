package soup.movie.ext

import soup.movie.model.Movie

fun Movie.getAgeLabel(): String = when {
    age >= 19 -> "청소년관람불가"
    age >= 15 -> "15세관람가"
    age >= 12 -> "12세관람가"
    age >= 0 -> "전체관람가"
    else -> "등급 미지정"
}
