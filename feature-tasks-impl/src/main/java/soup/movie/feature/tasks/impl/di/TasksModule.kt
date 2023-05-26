package soup.movie.feature.tasks.impl.di

import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soup.movie.feature.tasks.AnnounceOpenDateTasks
import soup.movie.feature.tasks.RecommendMoviesTasks
import soup.movie.feature.tasks.SyncOpenDateTasks
import soup.movie.feature.tasks.impl.AnnounceOpenDateTasksImpl
import soup.movie.feature.tasks.impl.AnnounceOpenDateUseCase
import soup.movie.feature.tasks.impl.AnnounceOpenDateUseCaseImpl
import soup.movie.feature.tasks.impl.RecommendMoviesTasksImpl
import soup.movie.feature.tasks.impl.RecommendMoviesUseCase
import soup.movie.feature.tasks.impl.RecommendMoviesUseCaseImpl
import soup.movie.feature.tasks.impl.SyncOpenDateTasksImpl
import soup.movie.feature.tasks.impl.SyncOpenDateUseCase
import soup.movie.feature.tasks.impl.SyncOpenDateUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TasksModule {

    @Binds
    fun bindsRecommendMoviesTasks(
        impl: RecommendMoviesTasksImpl
    ): RecommendMoviesTasks

    @Binds
    fun bindsRecommendMoviesUseCase(
        impl: RecommendMoviesUseCaseImpl
    ): RecommendMoviesUseCase

    @Binds
    fun bindsAnnounceOpenDateTasks(
        impl: AnnounceOpenDateTasksImpl
    ): AnnounceOpenDateTasks

    @Binds
    fun bindsAnnounceOpenDateUseCase(
        impl: AnnounceOpenDateUseCaseImpl
    ): AnnounceOpenDateUseCase

    @Binds
    fun bindsSyncOpenDateTasks(
        impl: SyncOpenDateTasksImpl
    ): SyncOpenDateTasks

    @Binds
    fun bindsSyncOpenDateUseCase(
        impl: SyncOpenDateUseCaseImpl
    ): SyncOpenDateUseCase
}
