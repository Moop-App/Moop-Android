package soup.movie.data.model

import androidx.annotation.Keep

@Keep
enum class ThemeOption {
    Light, // 밝게
    Dark,  // 어둡게
    System, // 시스템 설정값 (Default)
    Battery // 절전모드에 어둡게
}
