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
package soup.movie.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import soup.movie.model.Movie

@Serializable
class MovieResponse(
    val id: String,
    val score: Int,
    val title: String,
    val posterUrl: String,
    val openDate: String,
    @SerialName("now")
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>? = null,
    val genres: List<String>? = null,
    val boxOffice: Int? = null,
    val theater: TheaterRatingsResponse,
)

fun MovieResponse.asModel(): Movie {
    return Movie(
        id = id,
        score = score,
        title = title,
        posterUrl = posterUrl.replaceFirst("http:", "https:"),
        openDate = openDate,
        isNow = isNow,
        age = age,
        nationFilter = nationFilter,
        genres = genres,
        boxOffice = boxOffice,
        theater = theater.asModel(),
    )
}
