package soup.movie.ui.main

import androidx.annotation.Keep
import androidx.fragment.app.Fragment

interface PanelProvider {

    fun showPanel(panelState: PanelData)

    fun hidePanel()

    fun panelIsShown(): Boolean
}

interface PanelConsumer {

    fun Fragment.showPanel(panelState: PanelData) {
        (activity as? PanelProvider)?.showPanel(panelState)
    }

    fun Fragment.hidePanel() {
        (activity as? PanelProvider)?.hidePanel()
    }

    fun Fragment.panelIsShown(): Boolean {
        return (activity as? PanelProvider)?.panelIsShown() ?: false
    }
}

@Keep
data class PanelData(
    val tag: String,
    val newFragment: () -> Fragment
)
