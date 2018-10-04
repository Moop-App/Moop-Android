package soup.movie.ui.theater.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import soup.movie.ui.theater.edit.cgv.CgvEditFragment
import soup.movie.ui.theater.edit.lotte.LotteEditFragment
import soup.movie.ui.theater.edit.megabox.MegaboxEditFragment

internal class TheaterEditPageAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = items[position]

    override fun getCount(): Int = items.size

    companion object {

        private val items by lazy {
            arrayOf(
                    CgvEditFragment.newInstance(),
                    LotteEditFragment.newInstance(),
                    MegaboxEditFragment.newInstance())
        }
    }
}
