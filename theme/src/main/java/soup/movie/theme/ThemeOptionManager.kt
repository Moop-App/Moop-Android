package soup.movie.theme

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class ThemeOptionManager(
    private val store: ThemeOptionStore
) {

    fun initialize() {
        AppCompatDelegate.setDefaultNightMode(store.restore().nightMode)
    }

    fun apply(themeOption: ThemeOption) {
        store.save(themeOption)
        AppCompatDelegate.setDefaultNightMode(themeOption.nightMode)
    }

    fun createOptions(): List<ThemeOption> {
        return if (isAtLeastQ()) {
            createOptionsAtLeastQ()
        } else {
            createOptionsAtPreQ()
        }
    }

    private fun createOptionsAtLeastQ(): List<ThemeOption> {
        return listOf(
            ThemeOption.Light,
            ThemeOption.Dark,
            ThemeOption.System
        )
    }

    private fun createOptionsAtPreQ(): List<ThemeOption> {
        return listOf(
            ThemeOption.Light,
            ThemeOption.Dark,
            ThemeOption.Battery
        )
    }

    companion object {

        private val ThemeOption.nightMode: Int
            get() = when (this) {
                ThemeOption.Light -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeOption.Dark -> AppCompatDelegate.MODE_NIGHT_YES
                ThemeOption.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeOption.Battery -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }

        val defaultThemeOption: ThemeOption
            get() = if (isAtLeastQ()) {
                ThemeOption.System
            } else {
                ThemeOption.Battery
            }

        private fun isAtLeastQ(): Boolean {
            return Build.VERSION.SDK_INT >= 29
        }
    }
}
