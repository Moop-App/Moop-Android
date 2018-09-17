package soup.movie.ui.helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import soup.movie.R
import soup.movie.ui.BaseFragment.OnBackListener
import soup.movie.ui.main.BaseTabFragment.OnReselectListener

class FragmentSceneRouter(private val fragmentManager: FragmentManager,
                          private val containerId: Int) {

    data class SceneData(val tag: String,
                         val isPersist: Boolean = true,
                         val newFragment: () -> Fragment)

    private var lastState: SceneData? = null

    fun goTo(now: SceneData) {
        val fragmentTransaction = fragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .setReorderingAllowed(true)

        val previous: SceneData? = lastState
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
