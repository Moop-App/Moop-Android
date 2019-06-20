package soup.movie.theme

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class ThemeOptionManager(
    private val store: ThemeOptionStore
) {

    private val defaultThemeOption: ThemeOption = if (isAtLeastQ()) {
        ThemeOption.System
    } else {
        ThemeOption.Battery
    }

    private val options: List<ThemeOption> = listOf(
        ThemeOption.Light,
        ThemeOption.Dark,
        defaultThemeOption
    )

    fun initialize() {
        val themeOption = store.restore().toThemeOption()
        AppCompatDelegate.setDefaultNightMode(themeOption.toNightMode())
    }

    fun apply(themeOption: ThemeOption) {
        store.save(themeOption.toOptionString())
        AppCompatDelegate.setDefaultNightMode(themeOption.toNightMode())
    }

    fun getOptions(): List<ThemeOption> {
        return options
    }

    fun getCurrentOption(): ThemeOption {
        return store.restore().toThemeOption()
    }

    private fun String?.toThemeOption(): ThemeOption {
        return when (this) {
            OPTION_LIGHT -> ThemeOption.Light
            OPTION_DARK -> ThemeOption.Dark
            else -> defaultThemeOption
        }
    }

    private fun ThemeOption.toOptionString(): String {
        return when (this) {
            ThemeOption.Light -> OPTION_LIGHT
            ThemeOption.Dark -> OPTION_DARK
            else -> ""
        }
    }

    private fun ThemeOption.toNightMode(): Int {
        return when (this) {
            ThemeOption.Light -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeOption.Dark -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeOption.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeOption.Battery -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
    }

    private fun isAtLeastQ(): Boolean {
        return Build.VERSION.SDK_INT >= 29
    }

    companion object {
        private const val OPTION_LIGHT = "light"
        private const val OPTION_DARK = "dark"
    }
}
