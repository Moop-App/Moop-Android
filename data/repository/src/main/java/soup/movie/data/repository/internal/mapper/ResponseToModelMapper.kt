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
package soup.movie.data.repository.internal.mapper

import soup.movie.data.api.response.ActorResponse
import soup.movie.data.api.response.AreaResponse
import soup.movie.data.api.response.BoxOfficeResponse
import soup.movie.data.api.response.CgvInfoResponse
import soup.movie.data.api.response.CompanyResponse
import soup.movie.data.api.response.ImdbInfoResponse
import soup.movie.data.api.response.LotteInfoResponse
import soup.movie.data.api.response.MegaboxInfoResponse
import soup.movie.data.api.response.MetascoreInfoResponse
import soup.movie.data.api.response.MovieDetailResponse
import soup.movie.data.api.response.MovieListResponse
import soup.movie.data.api.response.MovieResponse
import soup.movie.data.api.response.NaverInfoResponse
import soup.movie.data.api.response.RottenTomatoInfoResponse
import soup.movie.data.api.response.TheaterAreaGroupResponse
import soup.movie.data.api.response.TheaterAreaResponse
import soup.movie.data.api.response.TheaterRatingsResponse
import soup.movie.data.api.response.TheaterResponse
import soup.movie.data.api.response.TrailerResponse
import soup.movie.model.Actor
import soup.movie.model.Area
import soup.movie.model.BoxOffice
import soup.movie.model.CgvInfo
import soup.movie.model.Company
import soup.movie.model.ImdbInfo
import soup.movie.model.LotteInfo
import soup.movie.model.MegaboxInfo
import soup.movie.model.MetascoreInfo
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.MovieList
import soup.movie.model.NaverInfo
import soup.movie.model.RottenTomatoInfo
import soup.movie.model.Theater
import soup.movie.model.TheaterArea
import soup.movie.model.TheaterAreaGroup
import soup.movie.model.TheaterRatings
import soup.movie.model.Trailer

internal fun MovieListResponse.toMovieList() = MovieList(
    lastUpdateTime, list.map { it.toMovie() }
)

private fun MovieResponse.toMovie() = Movie(
    id, score, title, posterUrl, openDate, isNow, age, nationFilter, genres, boxOffice,
    theater.toTheaterRatings()
)

private fun TheaterRatingsResponse.toTheaterRatings() = TheaterRatings(cgv, lotte, megabox)

internal fun MovieDetailResponse.toMovieDetail() = MovieDetail(
    id,
    score,
    title,
    posterUrl,
    openDate,
    isNow,
    age,
    nationFilter,
    genres,
    boxOffice?.toBoxOffice(),
    showTm,
    nations,
    directors,
    actors?.map { it.toActor() },
    companies?.map { it.toCompany() },
    cgv?.toCgvInfo(),
    lotte?.toLotteInfo(),
    megabox?.toMegaboxInfo(),
    naver?.toNaverInfo(),
    imdb?.toImdbInfo(),
    rt?.toRottenTomatoInfo(),
    mc?.toMetascoreInfo(),
    plot,
    trailers?.map { it.toTrailer() }
)

private fun BoxOfficeResponse.toBoxOffice() = BoxOffice(rank, audiCnt, audiAcc)
private fun ActorResponse.toActor() = Actor(peopleNm, cast)
private fun CompanyResponse.toCompany() = Company(companyNm, companyPartNm)
private fun CgvInfoResponse.toCgvInfo() = CgvInfo(id, star, url)
private fun LotteInfoResponse.toLotteInfo() = LotteInfo(id, star, url)
private fun MegaboxInfoResponse.toMegaboxInfo() = MegaboxInfo(id, star, url)
private fun NaverInfoResponse.toNaverInfo() = NaverInfo(star, url)
private fun ImdbInfoResponse.toImdbInfo() = ImdbInfo(id, star, url)
private fun RottenTomatoInfoResponse.toRottenTomatoInfo() = RottenTomatoInfo(star)
private fun MetascoreInfoResponse.toMetascoreInfo() = MetascoreInfo(star)
private fun TrailerResponse.toTrailer() = Trailer(youtubeId, title, author, thumbnailUrl)

internal fun TheaterAreaGroupResponse.toTheaterAreaGroup() = TheaterAreaGroup(
    cgv.map { it.toTheaterArea() },
    lotte.map { it.toTheaterArea() },
    megabox.map { it.toTheaterArea() }
)

private fun TheaterAreaResponse.toTheaterArea() = TheaterArea(
    area.toArea(),
    theaterList.map { it.toTheater() }
)
private fun AreaResponse.toArea() = Area(code, name)
private fun TheaterResponse.toTheater() = Theater(type, code, name, lng, lat)
