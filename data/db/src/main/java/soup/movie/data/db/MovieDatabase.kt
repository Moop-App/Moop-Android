package soup.movie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.db.internal.converter.FavoriteMovieTypeConverters
import soup.movie.data.db.entity.FavoriteMovieEntity
import soup.movie.data.db.entity.OpenDateAlarmEntity
import soup.movie.data.db.dao.FavoriteMovieDao
import soup.movie.data.db.dao.OpenDateAlarmDao

@Database(
    entities = [
        FavoriteMovieEntity::class,
        OpenDateAlarmEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(FavoriteMovieTypeConverters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

    abstract fun openDateAlarmDao(): OpenDateAlarmDao
}
