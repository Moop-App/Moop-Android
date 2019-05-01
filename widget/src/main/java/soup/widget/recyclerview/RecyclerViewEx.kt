package soup.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e?.actionMasked == MotionEvent.ACTION_DOWN) {
            if (scrollState == SCROLL_STATE_SETTLING) {
                parent.requestDisallowInterceptTouchEvent(false)
                if (!canScrollVertically(-1) || !canScrollVertically(1)) {
                    stopScroll()
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }
}
