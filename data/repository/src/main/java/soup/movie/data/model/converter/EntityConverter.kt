package soup.movie.data.model.converter

import soup.movie.data.model.Movie
import soup.movie.data.model.MovieDetail
import soup.movie.data.model.MovieTheater
import soup.movie.data.model.entity.FavoriteMovie
import soup.movie.data.model.entity.OpenDateAlarm

interface EntityConverter {

    fun FavoriteMovie.toMovie(): Movie {
        return Movie(
            id = id,
            score = 0,
            title = title,
            _posterUrl = posterUrl,
            openDate = openDate,
            isNow = isNow,
            age = age,
            nationFilter = nationFilter,
            genres = genres,
            boxOffice = boxOffice?.rank,
            theater = MovieTheater(
                cgv = cgv,
                lotte = lotte,
                megabox = megabox
            )
        )
    }

    fun MovieDetail.toFavoriteMovie(): FavoriteMovie {
        return FavoriteMovie(
            id = id,
            title = title,
            posterUrl = posterUrl,
            openDate = openDate,
            isNow = isNow,
            age = age,
            nationFilter = nationFilter,
            genres = genres,
            boxOffice = boxOffice,
            showTm = showTm,
            nations = nations,
            directors = directors,
            actors = actors,
            companies = companies,
            cgv = cgv?.star,
            lotte = lotte?.star,
            megabox = megabox?.star,
            naver = naver,
            imdb = imdb,
            rt = rt?.star,
            mc = mc?.star,
            plot = plot,
            trailers = trailers
        )
    }

    fun Movie.toOpenDateAlarm(): OpenDateAlarm {
        return OpenDateAlarm(
            id = id,
            title = title,
            openDate = openDate
        )
    }

    fun MovieDetail.toOpenDateAlarm(): OpenDateAlarm {
        return OpenDateAlarm(
            id = id,
            title = title,
            openDate = openDate
        )
    }
}
