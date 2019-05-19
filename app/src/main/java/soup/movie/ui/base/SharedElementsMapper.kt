package soup.movie.ui.base

import android.view.View

interface SharedElementsMapper {

    fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
        // do nothing
    }
}