package soup.widget.recyclerview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class FixedLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    override fun canScrollHorizontally(): Boolean = false

    override fun canScrollVertically(): Boolean = false
}
