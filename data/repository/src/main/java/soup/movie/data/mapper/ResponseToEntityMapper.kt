package soup.movie.data.mapper

import soup.movie.data.model.entity.FavoriteMovieEntity
import soup.movie.data.model.entity.MovieEntity
import soup.movie.data.model.entity.OpenDateAlarmEntity
import soup.movie.data.model.response.MovieResponse
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

fun MovieDetail.toFavoriteMovie(): FavoriteMovieEntity {
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
