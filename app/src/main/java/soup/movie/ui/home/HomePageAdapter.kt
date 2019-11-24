package soup.movie.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import soup.movie.ui.home.now.HomeNowFragment
import soup.movie.ui.home.plan.HomePlanFragment
import soup.movie.ui.home.tab.HomeTabFragment

class HomePageAdapter(fragment: HomeFragment) : FragmentStateAdapter(fragment) {

    private val items= arrayOf<Fragment>(
        HomeNowFragment(),
        HomePlanFragment()
    )

    override fun createFragment(position: Int): Fragment = items[position]

    override fun getItemCount(): Int = items.size

    fun getFragment(position: Int): HomeTabFragment? {
        return items.getOrNull(position) as? HomeTabFragment
    }

    fun scrollToTop(position: Int) {
        getFragment(position)?.scrollToTop()
    }
}
