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
package soup.movie.data.network.impl

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import soup.movie.data.network.impl.OkHttpInterceptors.HEADER_USE_CACHE
import soup.movie.data.network.response.MovieDetailResponse
import soup.movie.data.network.response.MovieListResponse
import soup.movie.data.network.response.TheaterAreaGroupResponse

interface MovieApiService {

    // 현재상영작

    @GET("now.json")
    suspend fun getNowMovieList(): MovieListResponse

    @Headers(HEADER_USE_CACHE)
    @GET("now/lastUpdateTime.json")
    suspend fun getNowLastUpdateTime(): Long

    // 개봉예정작

    @GET("plan.json")
    suspend fun getPlanMovieList(): MovieListResponse

    @Headers(HEADER_USE_CACHE)
    @GET("plan/lastUpdateTime.json")
    suspend fun getPlanLastUpdateTime(): Long

    // 영화 상세정보
    @Headers(HEADER_USE_CACHE)
    @GET("detail/{movieId}.json")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetailResponse

    // 공통코드
    @GET("code.json")
    suspend fun getCodeList(): TheaterAreaGroupResponse
}
