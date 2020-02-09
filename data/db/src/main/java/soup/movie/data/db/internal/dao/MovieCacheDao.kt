package soup.movie.data.db.internal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import soup.movie.data.db.internal.entity.MovieListEntity

@Dao
internal interface MovieCacheDao {

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    fun getMovieListByType(type: String): Flow<MovieListEntity>

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    suspend fun findByType(type: String): MovieListEntity

    @Insert(onConflict = REPLACE)
    suspend fun insert(response: MovieListEntity)

    @Delete
    suspend fun delete(response: MovieListEntity)

    @Query("DELETE FROM cached_movie_list")
    suspend fun deleteAll()
}
