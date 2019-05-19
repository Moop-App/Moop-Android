package soup.movie.ui.helper

import androidx.fragment.app.FragmentManager
import soup.movie.ui.main.PanelData

class FragmentPanelRouter(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {

    fun show(state: PanelData) {
        val fragmentTransaction = fragmentManager.beginTransaction()
                .disallowAddToBackStack()

        fragmentManager.findFragmentByTag(state.tag)?.apply {
            if (isDetached) {
                fragmentTransaction.attach(this)
            } else {
                return
            }
        } ?: run {
            fragmentManager.findFragmentById(containerId)?.apply {
                if (isDetached.not()) {
                    fragmentTransaction.detach(this)
                }
            }
            fragmentTransaction.add(containerId, state.newFragment(), state.tag)
        }

        fragmentTransaction.commitNow()
    }

    fun hide() {
        fragmentManager.findFragmentById(containerId)?.let {
            if (it.isDetached.not()) {
                fragmentManager.beginTransaction()
                        .disallowAddToBackStack()
                        .detach(it)
                        .commitNow()
            }
        }
    }
}
