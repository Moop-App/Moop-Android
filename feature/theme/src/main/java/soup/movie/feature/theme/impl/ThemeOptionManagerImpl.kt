/*
 * Copyright 2022 SOUP
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
package soup.movie.feature.theme.impl

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import soup.movie.feature.theme.ThemeOption
import soup.movie.feature.theme.ThemeOptionManager
import javax.inject.Inject

class ThemeOptionManagerImpl @Inject constructor(
    private val store: ThemeOptionStore,
) : ThemeOptionManager {

    private val defaultThemeOption: ThemeOption =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ThemeOption.System
        } else {
            ThemeOption.Battery
        }

    private val options: List<ThemeOption> = listOf(
        ThemeOption.Light,
        ThemeOption.Dark,
        defaultThemeOption,
    )

    override fun initialize() {
        val themeOption = store.restore().toThemeOption()
        AppCompatDelegate.setDefaultNightMode(themeOption.toNightMode())
    }

    override fun apply(themeOption: ThemeOption) {
        store.save(themeOption.toOptionString())
        AppCompatDelegate.setDefaultNightMode(themeOption.toNightMode())
    }

    override fun getOptions(): List<ThemeOption> {
        return options
    }

    override fun getCurrentOption(): ThemeOption {
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

    companion object {
        private const val OPTION_LIGHT = "light"
        private const val OPTION_DARK = "dark"
    }
}
