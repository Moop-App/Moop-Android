package soup.movie.data.db.internal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Flowable
import soup.movie.data.db.entity.MovieListEntity

@Dao
interface MovieCacheDao {

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    fun getMovieListByType(type: String): Flowable<MovieListEntity>

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    suspend fun findByType(type: String): MovieListEntity

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    suspend fun getMovieListOf(type: String): MovieListEntity

    @Insert(onConflict = REPLACE)
    fun insert(response: MovieListEntity)

    @Delete
    fun delete(response: MovieListEntity)

    @Query("DELETE FROM cached_movie_list")
    fun deleteAll()
}
