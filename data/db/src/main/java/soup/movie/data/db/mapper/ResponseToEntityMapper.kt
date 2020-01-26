package soup.movie.data.db.mapper

import soup.movie.data.db.entity.FavoriteMovieEntity
import soup.movie.data.db.entity.MovieEntity
import soup.movie.data.db.entity.OpenDateAlarmEntity
import soup.movie.data.api.response.MovieResponse
import soup.movie.model.MovieDetail

fun MovieResponse.toMovieEntity(): MovieEntity {
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

fun MovieDetail.toFavoriteMovieEntity(): FavoriteMovieEntity {
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
        boxOffice = boxOffice?.rank,
        cgv = cgv?.star,
        lotte = lotte?.star,
        megabox = megabox?.star
    )
}

fun MovieResponse.toOpenDateAlarmEntity(): OpenDateAlarmEntity {
    return OpenDateAlarmEntity(
        movieId = id,
        title = title,
        openDate = openDate
    )
}

fun MovieDetail.toOpenDateAlarmEntity(): OpenDateAlarmEntity {
    return OpenDateAlarmEntity(
        movieId = id,
        title = title,
        openDate = openDate
    )
}
