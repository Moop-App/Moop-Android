package soup.movie.ui.home

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface HomeTabFragment {

    fun scrollToTop()

    fun RecyclerView.setRecycledViewPoolForMovie(pool: RecyclerView.RecycledViewPool) {
        setRecycledViewPool(pool)
        layoutManager?.run {
            if (this is LinearLayoutManager) {
                recycleChildrenOnDetach = true
            }
        }
    }
}
