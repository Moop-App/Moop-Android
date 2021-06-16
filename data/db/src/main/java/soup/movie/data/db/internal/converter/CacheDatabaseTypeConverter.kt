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
package soup.movie.data.db.internal.converter

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import soup.movie.data.db.internal.entity.MovieEntity

internal class CacheDatabaseTypeConverter {

    @TypeConverter
    fun fromString(string: String?): List<MovieEntity> {
        if (string == null) {
            return emptyList()
        }
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun toString(movies: List<MovieEntity>): String {
        return Json.encodeToString(movies)
    }
}
