package soup.movie.settings.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_SettingsAssistedInjectModule::class])
abstract class SettingsAssistedInjectModule
