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
package soup.movie.data.database.impl.converter

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object FavoriteMovieTypeConverters {

    @TypeConverter
    @JvmStatic
    fun fromString(string: String?): List<String>? {
        if (string == null) {
            return null
        }
        return Json.decodeFromString(string)
    }

    @TypeConverter
    @JvmStatic
    fun toString(value: List<String>?): String {
        return Json.encodeToString(value.orEmpty())
    }
}
