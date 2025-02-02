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
package soup.movie.data.database.impl.mapper

import soup.movie.data.database.impl.entity.FavoriteMovieEntity
import soup.movie.data.database.impl.entity.MovieEntity
import soup.movie.data.database.impl.entity.OpenDateAlarmEntity
import soup.movie.datetime.calculateDDay
import soup.movie.datetime.toLocalDate
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel
import soup.movie.model.TheaterRatingsModel
import java.time.LocalDate

fun MovieEntity.toMovie(today: LocalDate): MovieModel {
    val openLocalDate = openDate.toLocalDate()
    return MovieModel(
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
        theater = TheaterRatingsModel(cgv, lotte, megabox),
        openLocalDate = openLocalDate,
        dDay = if (!isNow) calculateDDay(openDate = openLocalDate, today = today) else null,
    )
}

fun FavoriteMovieEntity.toMovie(today: LocalDate): MovieModel {
    val openLocalDate = openDate.toLocalDate()
    return MovieModel(
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
        theater = TheaterRatingsModel(cgv, lotte, megabox),
        openLocalDate = openLocalDate,
        dDay = if (!isNow) calculateDDay(openDate = openLocalDate, today = today) else null,
    )
}

fun OpenDateAlarmEntity.toOpenDateAlarm(): OpenDateAlarmModel {
    return OpenDateAlarmModel(
        movieId = movieId,
        title = title,
        openDate = openDate,
    )
}
