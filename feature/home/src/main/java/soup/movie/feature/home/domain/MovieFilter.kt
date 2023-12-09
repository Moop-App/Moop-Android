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
package soup.movie.feature.home.domain

import soup.movie.model.MovieModel
import soup.movie.model.settings.AgeFilter
import soup.movie.model.settings.GenreFilter
import soup.movie.model.settings.GenreFilter.Companion.GENRE_ETC
import soup.movie.model.settings.TheaterFilter

class MovieFilter(
    private val theaterFilter: TheaterFilter,
    private val ageFilter: AgeFilter,
    private val genreFilter: GenreFilter,
) {

    operator fun invoke(movie: MovieModel): Boolean {
        return movie.isFilterBy(theaterFilter) &&
            movie.isFilterBy(ageFilter) &&
            movie.isFilterBy(genreFilter)
    }

    private fun MovieModel.isFilterBy(theaterFilter: TheaterFilter): Boolean {
        val isScreeningAtCgv = theater.cgv != null
        val isScreeningAtLotteCinema = theater.lotte != null
        val isScreeningAtMegabox = theater.megabox != null
        return theaterFilter.hasCgv() && isScreeningAtCgv ||
            theaterFilter.hasLotteCinema() && isScreeningAtLotteCinema ||
            theaterFilter.hasMegabox() && isScreeningAtMegabox
    }

    private fun MovieModel.isFilterBy(ageFilter: AgeFilter): Boolean {
        return (ageFilter.hasAll() && age < 12) ||
            (ageFilter.has12() && age in 12..14) ||
            (ageFilter.has15() && age in 15..18) ||
            (ageFilter.has19() && age >= 19)
    }

    private fun MovieModel.isFilterBy(genreFilter: GenreFilter): Boolean {
        return genres?.any { it !in genreFilter.blacklist }
            ?: (genres.isNullOrEmpty() && GENRE_ETC !in genreFilter.blacklist)
    }
}
