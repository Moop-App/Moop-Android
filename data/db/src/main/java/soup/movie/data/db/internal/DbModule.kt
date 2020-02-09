package soup.movie.data.db.internal

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import soup.movie.data.db.MoopDatabase

@Module(includes = [DbModule.Providers::class])
internal class DbModule {

    @Provides
    fun provideMoopDatabase(
        movieDb: MovieDatabase,
        cacheDb: MovieCacheDatabase
    ): MoopDatabase {
        return RoomDatabase(
            movieDb.favoriteMovieDao(),
            movieDb.openDateAlarmDao(),
            cacheDb.movieCacheDao()
        )
    }

    @Module
    internal object Providers {

        @Provides
        fun createMovieDatabase(
            context: Context
        ): MovieDatabase {
            return Room
                .databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie.db")
                .build()
        }

        @Provides
        fun createCacheDatabase(
            context: Context
        ): MovieCacheDatabase {
            return Room
                .databaseBuilder(context.applicationContext, MovieCacheDatabase::class.java, "moop.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
