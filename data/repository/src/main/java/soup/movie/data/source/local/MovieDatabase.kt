package soup.movie.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soup.movie.data.model.converter.FavoriteMovieTypeConverters
import soup.movie.data.model.entity.FavoriteMovie
import soup.movie.data.model.entity.OpenDateAlarm

@Database(
    entities = [
        FavoriteMovie::class,
        OpenDateAlarm::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(FavoriteMovieTypeConverters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

    abstract fun openDateAlarmDao(): OpenDateAlarmDao
}
