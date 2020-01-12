package soup.movie.data.source

import android.content.Context
import androidx.room.Room
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import soup.movie.data.source.local.MovieCacheDatabase
import soup.movie.data.source.local.LocalMoopDataSource
import soup.movie.data.source.local.MovieDatabase
import soup.movie.data.source.remote.MoopApiService

object MoopDataSourceFactory {

    /* Local */

    fun createLocalDataSource(context: Context): LocalMoopDataSource {
        val appContext = context.applicationContext
        val movieDB = createMovieDatabase(appContext)
        val cacheDB = createCacheDatabase(appContext)
        return LocalMoopDataSource(
            movieDB.favoriteMovieDao(),
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

    /* Remote */

    fun createMoopApiService(
        apiUrl: String,
        okHttpClient: OkHttpClient
    ): MoopApiService = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(MoopApiService::class.java)
}
