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
package soup.movie.model.settings

class AgeFilter(private val flags: Int) {

    fun hasAll(): Boolean = has(FLAG_AGE_ALL)
    fun has12(): Boolean = has(FLAG_AGE_12)
    fun has15(): Boolean = has(FLAG_AGE_15)
    fun has19(): Boolean = has(FLAG_AGE_19)

    private fun has(flag: Int): Boolean {
        return (flags and flag) != 0
    }

    fun toFlags(): Int = flags

    companion object {

        const val FLAG_AGE_ALL: Int = 0x1
        const val FLAG_AGE_12: Int = 0x2
        const val FLAG_AGE_15: Int = 0x4
        const val FLAG_AGE_19: Int = 0x8
        const val FLAG_AGE_DEFAULT: Int = FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15 or FLAG_AGE_19
    }
}
