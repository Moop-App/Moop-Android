@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.Fragment

inline val Activity.isPortrait: Boolean
    get() = resources.isPortrait

inline val Activity.isLandscape: Boolean
    get() = resources.isLandscape

inline val Fragment.isPortrait: Boolean
    get() = resources.isPortrait

inline val Fragment.isLandscape: Boolean
    get() = resources.isLandscape

inline val View.isPortrait: Boolean
    get() = resources.isPortrait

inline val View.isLandscape: Boolean
    get() = resources.isPortrait

inline val Context.isPortrait: Boolean
    get() = resources.isPortrait

inline val Context.isLandscape: Boolean
    get() = resources.isLandscape

inline val Resources.isPortrait: Boolean
    get() = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

inline val Resources.isLandscape: Boolean
    get() = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
