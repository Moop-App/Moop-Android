package soup.movie.ui.theme

import androidx.annotation.Keep

@Keep
data class ThemeOptionUiModel(
    val name: String,
    val nightMode: Int
)

@Keep
enum class ThemeState {
    Light, // 밝게
    Dark,  // 어둡게
    System // 시스템 설정값 (Default)
}
