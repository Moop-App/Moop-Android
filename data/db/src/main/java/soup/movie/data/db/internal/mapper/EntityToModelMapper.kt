package soup.movie.data.db.internal.mapper

import soup.movie.data.db.internal.entity.FavoriteMovieEntity
import soup.movie.data.db.internal.entity.MovieEntity
import soup.movie.data.db.internal.entity.OpenDateAlarmEntity
import soup.movie.model.Movie
import soup.movie.model.OpenDateAlarm
import soup.movie.model.TheaterRatings

internal fun MovieEntity.toMovie() = Movie(
    id, score, title, posterUrl, openDate, isNow, age, nationFilter, genres, boxOffice,
    TheaterRatings(cgv, lotte, megabox)
)

internal fun FavoriteMovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        score = score,
        title = title,
        posterUrl = posterUrl,
        openDate = openDate,
        isNow = isNow,
        age = age,
        nationFilter = nationFilter,
        genres = genres,
        boxOffice = boxOffice,
        theater = TheaterRatings(cgv, lotte, megabox)
    )
}

internal fun OpenDateAlarmEntity.toOpenDateAlarm() = OpenDateAlarm(movieId, title, openDate)
