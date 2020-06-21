package soup.movie.theme

import android.widget.TextView

fun TextView.setThemeOptionLabel(themeOption: ThemeOption?) {
    val resId = when (themeOption) {
        ThemeOption.Light -> R.string.theme_option_light
        ThemeOption.Dark -> R.string.theme_option_dark
        ThemeOption.Battery -> R.string.theme_option_battery_saver
        ThemeOption.System -> R.string.theme_option_system
        else -> R.string.theme_option_system
    }
    setText(resId)
}
