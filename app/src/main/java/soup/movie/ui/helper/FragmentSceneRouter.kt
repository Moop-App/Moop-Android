package soup.movie.ui.helper

import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import soup.movie.R
import soup.movie.ui.BaseFragment.OnBackListener
import soup.movie.ui.main.BaseTabFragment.OnReselectListener

class FragmentSceneRouter(private val fragmentManager: FragmentManager,
                          private val containerId: Int) {

    data class SceneData(val tag: String,
                         val isPersist: Boolean = true,
                         val animate: Boolean = true,
                         val newFragment: () -> Fragment) {

        @AnimRes val enter: Int = if (animate) R.anim.fade_in else 0
        @AnimRes val exit: Int = if (animate) R.anim.fade_out else 0
    }

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
            if (isHidden) {
                fragmentTransaction.show(this)
            }
        } ?: run {
            fragmentTransaction.add(containerId, now.newFragment(), now.tag)
        }
        lastState = now

        fragmentTransaction.commitNow()
    }

    fun goBack(): Boolean {
        return (fragmentManager.findFragmentById(containerId) as? OnBackListener)?.onBackPressed()
                ?: false
    }

    fun reselectTab() {
        (fragmentManager.findFragmentById(containerId) as? OnReselectListener)?.onReselect()
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fragmentManager.findFragmentById(containerId)?.run {
            onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
