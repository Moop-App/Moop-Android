package soup.movie.feature.settings.impl

import androidx.compose.runtime.Composable
import soup.movie.feature.settings.SettingsComposableFactory
import javax.inject.Inject

class SettingsComposableFactoryImpl @Inject constructor(
) : SettingsComposableFactory {

    @Composable
    override fun SettingsScreen() {
        SettingsNavGraph()
    }
}
