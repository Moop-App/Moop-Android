package soup.movie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.db.entity.MovieListEntity
import soup.movie.data.db.internal.converter.CacheDatabaseTypeConverter
import soup.movie.data.db.internal.dao.MovieCacheDao

@Database(entities = [MovieListEntity::class], version = 4, exportSchema = false)
@TypeConverters(CacheDatabaseTypeConverter::class)
abstract class MovieCacheDatabase : RoomDatabase() {

    abstract fun movieCacheDao(): MovieCacheDao
}
