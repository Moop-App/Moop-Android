package soup.movie.data.source

import android.content.Context
import androidx.room.Room
import soup.movie.data.source.local.LocalMoopDataSource
import soup.movie.data.source.local.MovieCacheDatabase
import soup.movie.data.source.local.MovieDatabase

object MoopDataSourceFactory {

    /* Local */

    fun createLocalDataSource(context: Context): LocalMoopDataSource {
        val appContext = context.applicationContext
        val movieDB = createMovieDatabase(appContext)
        val cacheDB = createCacheDatabase(appContext)
        return LocalMoopDataSource(
            movieDB.favoriteMovieDao(),
            movieDB.openDateAlarmDao(),
            cacheDB.movieCacheDao()
        )
    }

    private fun createMovieDatabase(
        context: Context
    ): MovieDatabase = Room
        .databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie.db")
        .build()

    private fun createCacheDatabase(
        context: Context
    ): MovieCacheDatabase = Room
        .databaseBuilder(context.applicationContext, MovieCacheDatabase::class.java, "moop.db")
        .fallbackToDestructiveMigration()
        .build()
}
