package soup.movie.feature.navigator

import androidx.navigation3.runtime.NavKey

interface ScreenKey : NavKey {
    companion object {
        const val SCENE_KEY_ROOT: String = "root"
        const val SCENE_KEY_SETTINGS: String = "settings"
    }
}
