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
import soup.movie.model.TheaterModel
import soup.movie.model.TheaterTypeModel

@Serializable
class TheaterResponse(
    val type: String,
    val code: String,
    val name: String,
    val lng: Double,
    val lat: Double,
)

fun TheaterResponse.asModel(): TheaterModel {
    return TheaterModel(
        id = "$type:$code",
        type = TheaterTypeParser.parse(type),
        code = code,
        name = name,
        lng = lng,
        lat = lat,
    )
}

private object TheaterTypeParser {

    fun parse(type: String): TheaterTypeModel {
        return when (type) {
            "C" -> TheaterTypeModel.CGV
            "L" -> TheaterTypeModel.LOTTE
            "M" -> TheaterTypeModel.MEGABOX
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }
}
