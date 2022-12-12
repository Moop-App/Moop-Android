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
package soup.movie.core.designsystem.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Colors.divider: Color
    get() = if (isLight) {
        Color(0xFFF5F5F5)
    } else {
        Color(0xFF212121)
    }

val Colors.star: Color
    get() = Color(0xFFFFC107)

val Colors.detailShareDim: Color
    get() = if (isLight) {
        Color(0xDDFFFFFF)
    } else {
        Color(0xAA000000)
    }

val Colors.naver: Color
    get() = Color(0xFF1EC800)

val Colors.cgvBg: Color
    get() = Color.White

val Colors.cgvText: Color
    get() = Color(0xFFE51F20)

val Colors.lotteBg: Color
    get() = Color(0xFFED1D24)

val Colors.lotteText: Color
    get() = Color.White

val Colors.megaboxBg: Color
    get() = Color(0xFF352263)

val Colors.megaboxText: Color
    get() = Color.White
