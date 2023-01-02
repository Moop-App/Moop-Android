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
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel

fun MovieModel.toMovieEntity(): MovieEntity {
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

fun MovieModel.toFavoriteMovieEntity(): FavoriteMovieEntity {
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

fun MovieModel.toOpenDateAlarmEntity(): OpenDateAlarmEntity {
    return OpenDateAlarmEntity(
        movieId = id,
        title = title,
        openDate = openDate
    )
}

fun OpenDateAlarmModel.toEntity(): OpenDateAlarmEntity {
    return OpenDateAlarmEntity(
        movieId = movieId,
        title = title,
        openDate = openDate
    )
}
