package soup.movie.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import soup.movie.feature.navigator.Navigator
import soup.movie.ui.main.NavigatorImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
interface AppModule {

    @Binds
    fun bindsNavigator(
        impl: NavigatorImpl,
    ): Navigator
}
