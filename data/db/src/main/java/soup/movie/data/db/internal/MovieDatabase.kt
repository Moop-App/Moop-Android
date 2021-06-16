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
package soup.movie.data.db.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.db.internal.converter.FavoriteMovieTypeConverters
import soup.movie.data.db.internal.dao.FavoriteMovieDao
import soup.movie.data.db.internal.dao.OpenDateAlarmDao
import soup.movie.data.db.internal.entity.FavoriteMovieEntity
import soup.movie.data.db.internal.entity.OpenDateAlarmEntity

@Database(
    entities = [
        FavoriteMovieEntity::class,
        OpenDateAlarmEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(FavoriteMovieTypeConverters::class)
internal abstract class MovieDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

    abstract fun openDateAlarmDao(): OpenDateAlarmDao
}
