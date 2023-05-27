/*
 * Copyright 2023 SOUP
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
package soup.movie.feature.tasks.impl

import soup.movie.data.repository.MovieRepository
import soup.movie.domain.movie.isBest
import soup.movie.feature.tasks.NotificationBuilder
import soup.movie.model.MovieModel
import javax.inject.Inject

interface RecommendMoviesUseCase {
    suspend operator fun invoke()
}

class RecommendMoviesUseCaseImpl @Inject constructor(
    private val repository: MovieRepository,
    private val notificationBuilder: NotificationBuilder,
) : RecommendMoviesUseCase {

    override suspend fun invoke() {
        val movieList = getRecommendedMovieList()
        if (movieList.isNotEmpty()) {
            notificationBuilder.showLegacyNotification(movieList)
        }
    }

    private suspend fun getRecommendedMovieList(): List<MovieModel> {
        return repository.updateAndGetNowMovieList().asSequence()
            .filter {
                it.theater.run {
                    cgv != null && lotte != null && megabox != null
                }
            }
            .filterIndexed { index, movie -> index < 3 || movie.isBest() }
            .take(6)
            .toList()
    }
}
