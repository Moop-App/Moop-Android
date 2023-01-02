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
import soup.movie.model.MovieDetailModel

/**
 * @param genres 장르
 * @param nations 국가
 * @param companies 배급사/제작사
 * @param directors 감독
 * @param actors 배우
 * @param showTm 상영시간 (분)
 * @param boxOffice 박스오피스 정보
 */
@Serializable
class MovieDetailResponse(
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

    val boxOffice: BoxOfficeResponse? = null,
    val showTm: Int? = null,
    val nations: List<String>? = null,
    val directors: List<String>? = null,
    val actors: List<ActorResponse>? = null,
    val companies: List<CompanyResponse>? = null,
    val cgv: CgvInfoResponse? = null,
    val lotte: LotteInfoResponse? = null,
    val megabox: MegaboxInfoResponse? = null,
    val naver: NaverInfoResponse? = null,
    val imdb: ImdbInfoResponse? = null,
    val rt: RottenTomatoInfoResponse? = null,
    val mc: MetascoreInfoResponse? = null,
    val plot: String? = null,
    val trailers: List<TrailerResponse>? = null,
)

fun MovieDetailResponse.asModel(): MovieDetailModel {
    return MovieDetailModel(
        id = id,
        score = score,
        title = title,
        posterUrl = posterUrl.replaceFirst("http:", "https:"),
        openDate = openDate,
        isNow = isNow,
        age = age,
        nationFilter = nationFilter,
        genres = genres,
        boxOffice = boxOffice?.asModel(),
        showTm = showTm,
        nations = nations,
        directors = directors,
        actors = actors?.map { it.asModel() },
        companies = companies?.map { it.asModel() },
        cgv = cgv?.asModel(),
        lotte = lotte?.asModel(),
        megabox = megabox?.asModel(),
        naver = naver?.asModel(),
        imdb = imdb?.asModel(),
        rt = rt?.asModel(),
        mc = mc?.asModel(),
        plot = plot,
        trailers = trailers?.map { it.asModel() },
    )
}
