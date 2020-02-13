package soup.movie.data.db.internal.mapper

import soup.movie.data.db.internal.entity.FavoriteMovieEntity
import soup.movie.data.db.internal.entity.MovieEntity
import soup.movie.data.db.internal.entity.OpenDateAlarmEntity
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.OpenDateAlarm

internal fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
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
        cgv = theater.cgv,
        lotte = theater.lotte,
        megabox = theater.megabox
    )
}

internal fun Movie.toFavoriteMovieEntity(): FavoriteMovieEntity {
    return FavoriteMovieEntity(
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
        cgv = theater.cgv,
        lotte = theater.lotte,
        megabox = theater.megabox
    )
}

internal fun Movie.toOpenDateAlarmEntity(): OpenDateAlarmEntity {
    return OpenDateAlarmEntity(
        movieId = id,
        title = title,
        openDate = openDate
    )
}

internal fun OpenDateAlarm.toEntity(): OpenDateAlarmEntity {
    return OpenDateAlarmEntity(
        movieId = movieId,
        title = title,
        openDate = openDate
    )
}
