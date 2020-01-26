package soup.movie.data.db.internal

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import soup.movie.data.db.LocalMoopDataSource
import soup.movie.data.db.MovieCacheDatabase
import soup.movie.data.db.MovieDatabase

@Module(includes = [DbModule.Providers::class])
internal class DbModule {

    @Provides
    fun provideLocalDataSource(
        movieDb: MovieDatabase,
        cacheDb: MovieCacheDatabase
    ): LocalMoopDataSource {
        return LocalMoopDataSource(
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
        ): MovieDatabase = Room
            .databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie.db")
            .build()

        @Provides
        fun createCacheDatabase(
            context: Context
        ): MovieCacheDatabase = Room
            .databaseBuilder(context.applicationContext, MovieCacheDatabase::class.java, "moop.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
