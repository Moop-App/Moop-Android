/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.data.database.internal.mapper

import soup.movie.data.database.internal.entity.FavoriteMovieEntity
import soup.movie.data.database.internal.entity.MovieEntity
import soup.movie.data.database.internal.entity.OpenDateAlarmEntity
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
