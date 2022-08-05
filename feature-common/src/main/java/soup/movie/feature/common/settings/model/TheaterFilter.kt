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
package soup.movie.feature.common.settings.model

class TheaterFilter(private var flags: Int) {

    fun hasAllTheaters(): Boolean = has(FLAG_THEATER_ALL)
    fun hasNoTheaters(): Boolean = has(FLAG_THEATER_NONE)
    fun hasCgv(): Boolean = has(FLAG_THEATER_CGV)
    fun hasLotteCinema(): Boolean = has(FLAG_THEATER_LOTTE)
    fun hasMegabox(): Boolean = has(FLAG_THEATER_MEGABOX)

    private fun has(flag: Int): Boolean {
        return (flags and flag) != 0
    }

    fun addFlag(flag: Int): Boolean {
        val newFlags = flags or flag
        if (flags != newFlags) {
            flags = newFlags
            return true
        }
        return false
    }

    fun removeFlag(flag: Int): Boolean {
        val newFlags = flags and flag.inv()
        if (flags != newFlags) {
            flags = newFlags
            return true
        }
        return false
    }

    fun toggleFlag(flag: Int): Boolean {
        val newFlags = flags xor flag
        if (flags != newFlags) {
            flags = newFlags
            return true
        }
        return false
    }

    fun toFlags(): Int = flags

    companion object {

        const val FLAG_THEATER_NONE: Int = 0
        const val FLAG_THEATER_CGV: Int = 1 shl 0
        const val FLAG_THEATER_LOTTE: Int = 1 shl 1
        const val FLAG_THEATER_MEGABOX: Int = 1 shl 2
        const val FLAG_THEATER_ALL: Int = FLAG_THEATER_CGV or FLAG_THEATER_LOTTE or FLAG_THEATER_MEGABOX
    }
}
