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
package soup.movie.data.network

import soup.movie.data.network.response.MovieDetailResponse
import soup.movie.data.network.response.MovieListResponse
import soup.movie.data.network.response.TheaterAreaGroupResponse

interface RemoteDataSource {

    // 현재상영작
    suspend fun getNowMovieList(): MovieListResponse
    suspend fun getNowLastUpdateTime(): Long

    // 개봉예정작
    suspend fun getPlanMovieList(): MovieListResponse
    suspend fun getPlanLastUpdateTime(): Long

    // 영화 상세정보
    suspend fun getMovieDetail(movieId: String): MovieDetailResponse

    // 공통코드
    suspend fun getCodeList(): TheaterAreaGroupResponse
}
