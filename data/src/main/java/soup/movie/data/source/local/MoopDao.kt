package soup.movie.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import soup.movie.data.model.entity.CachedMovieList
import soup.movie.data.model.entity.FavoriteMovie

@Dao
interface MoopDao {

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    fun getMovieListByType(type: String): Flowable<CachedMovieList>

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    suspend fun findByType(type: String): CachedMovieList

    @Query("SELECT * FROM cached_movie_list WHERE type LIKE :type")
    suspend fun getMovieListOf(type: String): CachedMovieList

    @Insert(onConflict = REPLACE)
    fun insert(response: CachedMovieList)

    @Delete
    fun delete(response: CachedMovieList)

    @Query("DELETE FROM cached_movie_list")
    fun deleteAll()

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovieList(): Flow<List<FavoriteMovie>>

    @Insert(onConflict = REPLACE)
    suspend fun addFavoriteMovie(movie: FavoriteMovie)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun removeFavoriteMovie(movieId: String)

    @Query("SELECT COUNT(id) FROM favorite_movies WHERE id = :movieId")
    suspend fun getCountForFavoriteMovie(movieId: String): Int
}