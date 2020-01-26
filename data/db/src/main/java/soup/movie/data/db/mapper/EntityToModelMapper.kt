package soup.movie.data.db.mapper

import soup.movie.data.db.entity.FavoriteMovieEntity
import soup.movie.data.db.entity.MovieEntity
import soup.movie.model.Movie
import soup.movie.model.TheaterRatings

fun MovieEntity.toMovie() = Movie(
    id, score, title, posterUrl, openDate, isNow, age, nationFilter, genres, boxOffice,
    TheaterRatings(cgv, lotte, megabox)
)

fun FavoriteMovieEntity.toMovie(): Movie {
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
