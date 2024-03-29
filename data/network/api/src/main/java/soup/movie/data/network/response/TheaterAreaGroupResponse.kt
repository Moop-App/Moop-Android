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

import kotlinx.serialization.Serializable
import soup.movie.model.TheaterAreaGroupModel

@Serializable
class TheaterAreaGroupResponse(
    val lastUpdateTime: Long,
    val cgv: List<TheaterAreaResponse> = emptyList(),
    val lotte: List<TheaterAreaResponse> = emptyList(),
    val megabox: List<TheaterAreaResponse> = emptyList(),
)

fun TheaterAreaGroupResponse.asModel(): TheaterAreaGroupModel {
    return TheaterAreaGroupModel(
        cgv = cgv.map { it.asModel() },
        lotte = lotte.map { it.asModel() },
        megabox = megabox.map { it.asModel() },
    )
}
