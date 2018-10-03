package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefBooleanSetting

class UseWebLinkSetting(preferences: SharedPreferences) :
        PrefBooleanSetting(preferences, "use_web_link", true)
