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
package soup.movie.data.database.impl.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "score")
    val score: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "posterUrl")
    val posterUrl: String,
    @ColumnInfo(name = "openDate")
    val openDate: String,
    @ColumnInfo(name = "isNow")
    val isNow: Boolean,
    @ColumnInfo(name = "age")
    val age: Int,
    @ColumnInfo(name = "nationFilter")
    val nationFilter: List<String>? = emptyList(),
    @ColumnInfo(name = "genres")
    val genres: List<String>? = emptyList(),
    @ColumnInfo(name = "boxOffice")
    val boxOffice: Int? = null,
    @ColumnInfo(name = "cgv")
    val cgv: String? = null,
    @ColumnInfo(name = "lotte")
    val lotte: String? = null,
    @ColumnInfo(name = "megabox")
    val megabox: String? = null,
)
