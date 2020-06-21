package soup.movie.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import soup.movie.data.db.internal.MovieCacheDatabase
import soup.movie.data.db.internal.MovieDatabase
import soup.movie.data.db.internal.RoomDatabase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideMoopDatabase(
        @ApplicationContext context: Context
    ): MoopDatabase {
        val movieDb = createMovieDatabase(context)
        val cacheDb = createCacheDatabase(context)
        return RoomDatabase(
            movieDb.favoriteMovieDao(),
            movieDb.openDateAlarmDao(),
            cacheDb.movieCacheDao()
        )
    }

    private fun createMovieDatabase(
        context: Context
    ): MovieDatabase {
        return Room
            .databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie.db")
            .build()
    }

    private fun createCacheDatabase(
        context: Context
    ): MovieCacheDatabase {
        return Room
            .databaseBuilder(context.applicationContext, MovieCacheDatabase::class.java, "moop.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
