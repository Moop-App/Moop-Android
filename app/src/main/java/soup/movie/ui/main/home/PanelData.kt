package soup.movie.ui.main.home

import androidx.annotation.Keep
import androidx.fragment.app.Fragment

@Keep
class PanelData(
    val tag: String,
    val newFragment: () -> Fragment
)
