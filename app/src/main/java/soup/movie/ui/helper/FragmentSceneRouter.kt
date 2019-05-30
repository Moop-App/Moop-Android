package soup.movie.ui.helper

import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import soup.movie.R
import soup.movie.ui.base.OnBackPressedListener

class FragmentSceneRouter(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {

    data class SceneData(
        val tag: String,
        val isPersist: Boolean = false,
        @AnimRes val enter: Int = R.anim.fade_in,
        @AnimRes val exit: Int = R.anim.fade_out,
        val newFragment: () -> Fragment
    )

    private var lastState: SceneData? = null

    fun goTo(now: SceneData) {
        val previous: SceneData? = lastState

        val fragmentTransaction = fragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(now.enter, previous?.exit ?: 0)
                .setReorderingAllowed(true)

        if (previous != null) {
            fragmentManager.findFragmentByTag(previous.tag)?.apply {
                if (!previous.isPersist) {
                    fragmentTransaction.remove(this)
                } else if (!isHidden) {
                    fragmentTransaction.hide(this)
                }
            }
        }

        fragmentManager.findFragmentByTag(now.tag)?.apply {
            when {
                isHidden -> fragmentTransaction.show(this)
                isRemoving -> fragmentTransaction.add(containerId, now.newFragment(), now.tag)
            }
        } ?: run {
            fragmentTransaction.add(containerId, now.newFragment(), now.tag)
        }
        lastState = now

        fragmentTransaction.commitNow()
    }

    fun goBack(): Boolean {
        return (fragmentManager.findFragmentById(containerId) as? OnBackPressedListener)?.onBackPressed()
                ?: false
    }
}
