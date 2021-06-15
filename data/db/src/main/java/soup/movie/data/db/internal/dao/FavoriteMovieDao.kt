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
package soup.movie.data.db.internal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import soup.movie.data.db.internal.entity.FavoriteMovieEntity

@Dao
internal interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovieList(): Flow<List<FavoriteMovieEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertFavoriteMovie(movie: FavoriteMovieEntity)

    @Update(onConflict = REPLACE)
    suspend fun updateAll(movies: List<FavoriteMovieEntity>)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteFavoriteMovie(movieId: String)

    @Query("SELECT COUNT(id) FROM favorite_movies WHERE id = :movieId")
    suspend fun getCountForFavoriteMovie(movieId: String): Int

    suspend fun isFavoriteMovie(movieId: String): Boolean {
        return getCountForFavoriteMovie(movieId) > 0
    }
}
