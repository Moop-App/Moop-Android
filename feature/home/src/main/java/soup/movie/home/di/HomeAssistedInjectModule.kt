package soup.movie.home.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_HomeAssistedInjectModule::class])
abstract class HomeAssistedInjectModule
