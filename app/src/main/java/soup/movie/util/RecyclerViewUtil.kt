package soup.movie.util

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager

object RecyclerViewUtil {

    @JvmStatic
    fun verticalLinearLayoutManager(context: Context): LinearLayoutManager {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        return layoutManager
    }

    @JvmStatic
    fun horizontalLinearLayoutManager(context: Context): LinearLayoutManager {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        return layoutManager
    }

    @JvmStatic
    fun gridLayoutManager(context: Context,
                          spanCount: Int): GridLayoutManager {
        return GridLayoutManager(context, spanCount)
    }
}
