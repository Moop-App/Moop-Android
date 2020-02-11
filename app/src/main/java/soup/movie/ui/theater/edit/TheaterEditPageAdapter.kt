package soup.movie.ui.theater.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TitleProvider
import soup.movie.ext.lazyFast

class TheaterEditPageAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle), TitleProvider {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CgvEditFragment()
            1 -> LotteEditFragment()
            else -> MegaboxEditFragment()
        }
    }

    override fun getItemCount(): Int = 3

    override fun getItemTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "CGV"
            1 -> "롯데시네마"
            else -> "메가박스"
        }
    }

    companion object {

        private val items by lazyFast {
            arrayOf(
                CgvEditFragment(),
                LotteEditFragment(),
                MegaboxEditFragment()
            )
        }
    }
}
