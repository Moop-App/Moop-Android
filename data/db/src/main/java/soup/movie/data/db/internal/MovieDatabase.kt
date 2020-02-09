package soup.movie.data.db.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.db.internal.entity.FavoriteMovieEntity
import soup.movie.data.db.internal.entity.OpenDateAlarmEntity
import soup.movie.data.db.internal.converter.FavoriteMovieTypeConverters
import soup.movie.data.db.internal.dao.FavoriteMovieDao
import soup.movie.data.db.internal.dao.OpenDateAlarmDao

@Database(
    entities = [
        FavoriteMovieEntity::class,
        OpenDateAlarmEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(FavoriteMovieTypeConverters::class)
internal abstract class MovieDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

    abstract fun openDateAlarmDao(): OpenDateAlarmDao
}
