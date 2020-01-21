package soup.movie.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import soup.movie.data.model.Movie

@Entity(tableName = "cached_movie_list")
data class CachedMovieList(
    @PrimaryKey
    val type: String,
    val lastUpdateTime: Long,
    val list: List<Movie>
) {

    companion object {

        const val TYPE_NOW = "type_now"
        const val TYPE_PLAN = "type_plan"

        fun empty(type: String): CachedMovieList =
            CachedMovieList(type, 0, emptyList())
    }
}
