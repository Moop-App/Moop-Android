/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.ext

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.Fragment

/* isPortrait */

inline val Activity.isPortrait: Boolean
    get() = resources.isPortrait

inline val Fragment.isPortrait: Boolean
    get() = resources.isPortrait

inline val View.isPortrait: Boolean
    get() = resources.isPortrait

inline val Context.isPortrait: Boolean
    get() = resources.isPortrait

inline val Resources.isPortrait: Boolean
    get() = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

/* isLandscape */

inline val Activity.isLandscape: Boolean
    get() = resources.isLandscape

inline val Fragment.isLandscape: Boolean
    get() = resources.isLandscape

inline val View.isLandscape: Boolean
    get() = resources.isPortrait

inline val Context.isLandscape: Boolean
    get() = resources.isLandscape

inline val Resources.isLandscape: Boolean
    get() = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

/* isLightTheme */

inline val Activity.isLightTheme: Boolean
    get() = resources.isLightTheme

inline val Fragment.isLightTheme: Boolean
    get() = resources.isLightTheme

inline val View.isLightTheme: Boolean
    get() = resources.isLightTheme

inline val Context.isLightTheme: Boolean
    get() = resources.isLightTheme

inline val Resources.isLightTheme: Boolean
    get() = configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK ==
        Configuration.UI_MODE_NIGHT_NO

/* isDarkTheme */

inline val Activity.isDarkTheme: Boolean
    get() = resources.isDarkTheme

inline val Fragment.isDarkTheme: Boolean
    get() = resources.isDarkTheme

inline val View.isDarkTheme: Boolean
    get() = resources.isDarkTheme

inline val Context.isDarkTheme: Boolean
    get() = resources.isDarkTheme

inline val Resources.isDarkTheme: Boolean
    get() = configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK ==
        Configuration.UI_MODE_NIGHT_YES
