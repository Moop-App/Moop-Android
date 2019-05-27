package soup.movie.ui.theme

import android.widget.TextView
import androidx.databinding.BindingAdapter
import soup.movie.theme.ThemeOption

@BindingAdapter("themeOptionLabel")
fun setThemeOptionLabel(textView: TextView, themeOption: ThemeOption?) {
    //TODO: hard-coded labels
    textView.text = when (themeOption) {
        ThemeOption.Light -> "밝게"
        ThemeOption.Dark -> "어둡게"
        ThemeOption.Battery -> "절전 모드 설정"
        ThemeOption.System -> "시스템 기본값"
        else -> ""
    }
}
