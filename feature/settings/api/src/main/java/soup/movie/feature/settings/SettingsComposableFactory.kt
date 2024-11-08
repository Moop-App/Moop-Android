package soup.movie.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

interface SettingsComposableFactory {
    @Composable fun SettingsScreen()
}

@Composable
fun rememberSettingsComposableFactory(): SettingsComposableFactory {
    val context = LocalContext.current
    return remember(context) {
        EntryPointAccessors
            .fromApplication(context, SettingsComposableFactoryEntryPoint::class.java)
            .settingsComposableFactory()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SettingsComposableFactoryEntryPoint {
    fun settingsComposableFactory(): SettingsComposableFactory
}
