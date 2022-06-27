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
package soup.movie.detail

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

internal val Colors.star: Color
    get() = Color(0xFFFFC107)

internal val Colors.detailShareDim: Color
    get() = if (isLight) {
        Color(0xDDFFFFFF)
    } else {
        Color(0xAA000000)
    }

internal val Colors.naver: Color
    get() = Color(0xFF1EC800)
