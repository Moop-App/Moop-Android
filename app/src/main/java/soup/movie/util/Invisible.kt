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
package soup.movie.util

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.platform.debugInspectorInfo

fun Modifier.invisible(
    invisible: Boolean,
    backgroundColor: Color = Color.Unspecified
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "invisible"
        value = invisible
        properties["invisible"] = invisible
    }
) {
    if (invisible) {
        val color = if (backgroundColor.isUnspecified) {
            MaterialTheme.colors.background
        } else {
            backgroundColor
        }
        drawWithContent {
            drawRect(color = color)
        }
    } else {
        this
    }
}
