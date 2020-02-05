package androidx.recyclerview.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.ext.FixedLayoutManager
import soup.movie.core.R

class RecyclerViewEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    init {
        if (attrs != null) {
            val defStyleRes = 0
            val a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewEx, defStyle, defStyleRes)
            val layoutManagerType = a.getInt(R.styleable.RecyclerViewEx_layoutManagerType, TYPE_NONE)
            when (layoutManagerType) {
                TYPE_LINEAR -> LinearLayoutManager(context, attrs, defStyle, defStyleRes)
                TYPE_GRID -> GridLayoutManager(context, attrs, defStyle, defStyleRes)
                TYPE_FIXED -> FixedLayoutManager(context, attrs, defStyle, defStyleRes)
                else -> null
            }?.let { layoutManager = it }
            a.recycle()
        }
    }

    // workaround codes for fling events
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

    companion object {

        private const val TYPE_NONE = 0
        private const val TYPE_LINEAR = 1
        private const val TYPE_GRID = 2
        private const val TYPE_FIXED = 3
    }
}
