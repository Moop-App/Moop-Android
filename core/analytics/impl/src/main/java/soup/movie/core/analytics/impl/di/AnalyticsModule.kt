package soup.movie.core.analytics.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soup.movie.core.analytics.EventAnalytics
import soup.movie.core.analytics.impl.EventAnalyticsImpl

@Module
@InstallIn(SingletonComponent::class)
interface AnalyticsModule {

    @Binds
    fun bindsEventAnalytics(
        impl: EventAnalyticsImpl,
    ): EventAnalytics
}
