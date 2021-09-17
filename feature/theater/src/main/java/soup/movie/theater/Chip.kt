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
package soup.movie.theater

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Chip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ChipElevation = ChipDefaults.elevation(),
    shape: Shape = RoundedCornerShape(percent = 50),
    border: BorderStroke? = null,
    colors: ChipColors = ChipDefaults.chipColors(),
    contentPadding: PaddingValues = ChipDefaults.ContentPadding,
    minTouchTargetSize: Dp = ChipDefaults.MinTouchTargetSize,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    val minTouchTargetSizeModifier = if (minTouchTargetSize > ChipDefaults.MinHeight) {
        Modifier.padding(vertical = (minTouchTargetSize - ChipDefaults.MinHeight) / 2f)
    } else {
        Modifier
    }
    Surface(
        modifier = modifier.then(minTouchTargetSizeModifier),
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation.elevation(enabled, interactionSource).value,
        onClick = onClick,
        enabled = enabled,
        role = Role.Button,
        interactionSource = interactionSource,
        indication = rememberRipple()
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.body2
            ) {
                Row(
                    Modifier
                        .defaultMinSize(minHeight = ChipDefaults.MinHeight)
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Stable
interface ChipElevation {

    @Composable
    fun elevation(enabled: Boolean, interactionSource: InteractionSource): State<Dp>
}

@Stable
interface ChipColors {

    @Composable
    fun backgroundColor(enabled: Boolean): State<Color>

    @Composable
    fun contentColor(enabled: Boolean): State<Color>
}

object ChipDefaults {
    private val ButtonHorizontalPadding = 12.dp
    private val ButtonVerticalPadding = 0.dp

    val ContentPadding = PaddingValues(
        start = ButtonHorizontalPadding,
        top = ButtonVerticalPadding,
        end = ButtonHorizontalPadding,
        bottom = ButtonVerticalPadding
    )

    val MinHeight = 32.dp
    val MinTouchTargetSize = 48.dp

    @Composable
    fun elevation(
        defaultElevation: Dp = 0.dp,
        pressedElevation: Dp = 3.dp,
        disabledElevation: Dp = 0.dp
    ): ChipElevation = object : ChipElevation {

        private val elevation = ButtonDefaults.elevation(
            defaultElevation = defaultElevation,
            pressedElevation = pressedElevation,
            disabledElevation = disabledElevation
        )

        @Composable
        override fun elevation(enabled: Boolean, interactionSource: InteractionSource): State<Dp> {
            return elevation.elevation(enabled = enabled, interactionSource = interactionSource)
        }
    }

    @Composable
    fun chipColors(
        backgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.10f)
            .compositeOver(MaterialTheme.colors.surface),
        contentColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.87f),
        disabledBackgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            .compositeOver(MaterialTheme.colors.surface),
        disabledContentColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.33f)
    ): ChipColors = object : ChipColors {

        private val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledBackgroundColor = disabledBackgroundColor,
            disabledContentColor = disabledContentColor,
        )

        @Composable
        override fun backgroundColor(enabled: Boolean): State<Color> {
            return buttonColors.backgroundColor(enabled = enabled)
        }

        @Composable
        override fun contentColor(enabled: Boolean): State<Color> {
            return buttonColors.contentColor(enabled = enabled)
        }
    }
}
