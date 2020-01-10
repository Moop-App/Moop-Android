package soup.movie.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.model.converter.FavoriteMovieTypeConverter
import soup.movie.data.model.converter.MovieListTypeConverter
import soup.movie.data.model.entity.CachedMovieList
import soup.movie.data.model.entity.FavoriteMovie

@Database(
    entities = [CachedMovieList::class, FavoriteMovie::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(value = [
    MovieListTypeConverter::class,
    FavoriteMovieTypeConverter::class
])
abstract class MoopDatabase : RoomDatabase() {

    abstract fun moopDao(): MoopDao
}
