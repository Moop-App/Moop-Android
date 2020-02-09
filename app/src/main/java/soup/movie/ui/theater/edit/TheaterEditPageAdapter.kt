package soup.movie.ui.theater.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TitleProvider
import soup.movie.ui.theater.edit.cgv.CgvEditFragment
import soup.movie.ui.theater.edit.lotte.LotteEditFragment
import soup.movie.ui.theater.edit.megabox.MegaboxEditFragment
import soup.movie.ext.lazyFast

class TheaterEditPageAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle), TitleProvider {

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemTitle(position: Int): CharSequence {
        return items[position].title
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
