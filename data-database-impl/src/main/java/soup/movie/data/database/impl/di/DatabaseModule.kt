/*
 * Copyright 2022 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.data.database.impl.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soup.movie.data.database.LocalDataSource
import soup.movie.data.database.impl.LocalDataSourceImpl
import soup.movie.data.database.impl.MovieCacheDatabase
import soup.movie.data.database.impl.MovieDatabase
import soup.movie.data.database.impl.dao.FavoriteMovieDao
import soup.movie.data.database.impl.dao.MovieCacheDao
import soup.movie.data.database.impl.dao.OpenDateAlarmDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {

    @Binds
    @Singleton
    fun provideLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource

    companion object {

        @Provides
        @Singleton
        fun provideFavoriteMovieDao(
            movieDatabase: MovieDatabase
        ): FavoriteMovieDao {
            return movieDatabase.favoriteMovieDao()
        }

        @Provides
        @Singleton
        fun provideOpenDateAlarmDao(
            movieDatabase: MovieDatabase
        ): OpenDateAlarmDao {
            return movieDatabase.openDateAlarmDao()
        }

        @Provides
        @Singleton
        fun provideMovieCacheDao(
            movieCacheDatabase: MovieCacheDatabase
        ): MovieCacheDao {
            return movieCacheDatabase.movieCacheDao()
        }

        @Provides
        @Singleton
        fun provideMovieDatabase(
            @ApplicationContext context: Context
        ): MovieDatabase {
            return Room
                .databaseBuilder(context, MovieDatabase::class.java, "movie.db")
                .build()
        }

        @Provides
        @Singleton
        fun provideMovieCacheDatabase(
            @ApplicationContext context: Context
        ): MovieCacheDatabase {
            return Room
                .databaseBuilder(context, MovieCacheDatabase::class.java, "moop.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
