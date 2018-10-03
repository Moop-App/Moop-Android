package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefBooleanSetting

class UsePaletteThemeSetting(preferences: SharedPreferences) :
        PrefBooleanSetting(preferences, "use_palette_theme", true)
