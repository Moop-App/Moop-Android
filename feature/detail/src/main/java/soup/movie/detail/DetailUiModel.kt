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
package soup.movie.detail

import androidx.annotation.Keep
import com.google.android.gms.ads.nativead.NativeAd
import soup.movie.model.Company
import soup.movie.model.Movie
import soup.movie.model.Trailer

@Keep
data class HeaderUiModel(
    val movie: Movie,
    val showTm: Int = 0,
    val nations: List<String> = emptyList(),
    val companies: List<Company> = emptyList()
)

@Keep
class ContentUiModel(
    val items: List<ContentItemUiModel>
)

@Keep
sealed class ContentItemUiModel

@Keep
class CgvItemUiModel(
    val movieId: String,
    val hasInfo: Boolean,
    val rating: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class LotteItemUiModel(
    val movieId: String,
    val hasInfo: Boolean,
    val rating: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class MegaboxItemUiModel(
    val movieId: String,
    val hasInfo: Boolean,
    val rating: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class NaverItemUiModel(
    val rating: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class BoxOfficeItemUiModel(
    val rank: Int,
    val rankDate: String,
    val audience: Int,
    val screenDays: Int,
    val rating: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class ImdbItemUiModel(
    val imdb: String,
    val rottenTomatoes: String,
    val metascore: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class PlotItemUiModel(
    val plot: String
) : ContentItemUiModel() {

    var isExpanded: Boolean = false
}

@Keep
class CastItemUiModel(
    val persons: List<PersonUiModel>
) : ContentItemUiModel()

@Keep
class PersonUiModel(
    val name: String,
    val cast: String,
    val query: String
)

@Keep
class AdItemUiModel(
    val nativeAd: NativeAd
) : ContentItemUiModel()

@Keep
class TrailerHeaderItemUiModel(
    val movieTitle: String
) : ContentItemUiModel()

@Keep
class TrailerItemUiModel(
    val trailer: Trailer
) : ContentItemUiModel()

@Keep
class TrailerFooterItemUiModel(
    val movieTitle: String
) : ContentItemUiModel()

val ContentItemUiModel.id: String
    get() = when (this) {
        is CgvItemUiModel -> "cgv"
        is LotteItemUiModel -> "lotte"
        is MegaboxItemUiModel -> "megabox"
        is NaverItemUiModel -> "naver"
        is BoxOfficeItemUiModel -> "boxoffice"
        is ImdbItemUiModel -> "imdb"
        is PlotItemUiModel -> "plot"
        is CastItemUiModel -> "cast"
        is AdItemUiModel -> "ad"
        is TrailerHeaderItemUiModel -> "t_header"
        is TrailerItemUiModel -> "t_${trailer.youtubeId}"
        is TrailerFooterItemUiModel -> "t_footer"
    }
