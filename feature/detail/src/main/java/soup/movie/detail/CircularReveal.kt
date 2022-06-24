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

import android.graphics.Path
import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.hypot

fun Modifier.circularReveal(
    visible: Boolean,
    center: Offset = Offset(0.5f, 0.5f),
): Modifier = composed(
    factory = {
        val progress = updateTransition(targetState = visible, label = "visible")
            .animateFloat(
                transitionSpec = { tween(durationMillis = 300) },
                label = "progress",
            ) {
                if (it) 1f else 0f
            }
        clip(
            CircularRevealShape(
                progress = progress.value,
                centerOffset = center,
            )
        )
    },
    inspectorInfo = debugInspectorInfo {
        name = "circularReveal"
        properties["visible"] = visible
        properties["revealFrom"] = center
    }
)

private class CircularRevealShape(
    @FloatRange(from = 0.0, to = 1.0)
    private val progress: Float,
    private val centerOffset: Offset,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return Outline.Generic(
            path = AndroidPath(
                Path().also {
                    it.addCircle(
                        centerOffset.x * size.width,
                        centerOffset.y * size.height,
                        longestDistanceToACorner(size, centerOffset) * progress,
                        Path.Direction.CW,
                    )
                }
            )
        )
    }

    private fun longestDistanceToACorner(size: Size, offset: Offset?): Float {
        if (offset == null) {
            return hypot(size.width / 2f, size.height / 2f)
        }

        val topLeft = hypot(offset.x, offset.y)
        val topRight = hypot(size.width - offset.x, offset.y)
        val bottomLeft = hypot(offset.x, size.height - offset.y)
        val bottomRight = hypot(size.width - offset.x, size.height - offset.y)
        return topLeft
            .coerceAtLeast(topRight)
            .coerceAtLeast(bottomLeft)
            .coerceAtLeast(bottomRight)
    }
}
