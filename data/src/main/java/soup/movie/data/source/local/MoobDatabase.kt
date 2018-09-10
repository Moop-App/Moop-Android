package soup.movie.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.model.converter.MovieListTypeConverter
import soup.movie.data.model.response.CachedMovieList

@Database(entities = [CachedMovieList::class], version = 1, exportSchema = false)
@TypeConverters(MovieListTypeConverter::class)
abstract class MoobDatabase : RoomDatabase() {

    abstract fun moobDao(): MoobDao
}