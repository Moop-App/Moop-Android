package soup.movie.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.model.converter.CacheDatabaseTypeConverter
import soup.movie.data.model.entity.MovieListEntity

@Database(entities = [MovieListEntity::class], version = 3, exportSchema = false)
@TypeConverters(CacheDatabaseTypeConverter::class)
abstract class MovieCacheDatabase : RoomDatabase() {

    abstract fun movieCacheDao(): MovieCacheDao
}
