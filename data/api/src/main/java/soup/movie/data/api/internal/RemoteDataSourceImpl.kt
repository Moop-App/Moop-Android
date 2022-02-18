/*
 * Copyright 2022 SOUP
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
package soup.movie.data.api.internal

import soup.movie.data.api.RemoteDataSource
import soup.movie.data.api.response.MovieDetailResponse
import soup.movie.data.api.response.MovieListResponse
import soup.movie.data.api.response.TheaterAreaGroupResponse

internal class RemoteDataSourceImpl(
    private val apiService: MovieApiService
) : RemoteDataSource {

    override suspend fun getNowMovieList(): MovieListResponse {
        return apiService.getNowMovieList()
    }

    override suspend fun getNowLastUpdateTime(): Long {
        return apiService.getNowLastUpdateTime()
    }

    override suspend fun getPlanMovieList(): MovieListResponse {
        return apiService.getPlanMovieList()
    }

    override suspend fun getPlanLastUpdateTime(): Long {
        return apiService.getPlanLastUpdateTime()
    }

    override suspend fun getMovieDetail(movieId: String): MovieDetailResponse {
        return apiService.getMovieDetail(movieId)
    }

    override suspend fun getCodeList(): TheaterAreaGroupResponse {
        return apiService.getCodeList()
    }
}
