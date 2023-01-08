package soup.movie.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Elevations(
    val card: Dp = 0.dp,
    val bottomSheet: Dp = 0.dp,
)

internal val LocalElevations = staticCompositionLocalOf { Elevations() }
