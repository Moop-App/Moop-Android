package soup.movie.ui.theater.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import soup.movie.ui.theater.edit.cgv.CgvEditFragment
import soup.movie.ui.theater.edit.lotte.LotteEditFragment
import soup.movie.ui.theater.edit.megabox.MegaboxEditFragment
import soup.movie.util.lazyFast

class TheaterEditPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = items[position]

    override fun getCount(): Int = items.size

    override fun getPageTitle(position: Int): CharSequence = items[position].title

    companion object {

        private val items by lazyFast {
            arrayOf(
                CgvEditFragment.newInstance(),
                LotteEditFragment.newInstance(),
                MegaboxEditFragment.newInstance()
            )
        }
    }
}
