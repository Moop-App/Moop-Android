package soup.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class RecyclerViewEx : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e?.actionMasked == MotionEvent.ACTION_DOWN) {
            if (scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                parent.requestDisallowInterceptTouchEvent(false)
                if (!canScrollVertically(-1) || !canScrollVertically(1)) {
                    stopScroll()
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (DEBUG) addOnScrollListener(listener)
    }

    override fun onDetachedFromWindow() {
        removeOnScrollListener(listener)
        if (DEBUG) super.onDetachedFromWindow()
    }

    companion object {

        private const val DEBUG = false

        private val listener = object : OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Timber.d(when (newState) {
                    SCROLL_STATE_IDLE -> "SCROLL_STATE_IDLE"
                    SCROLL_STATE_DRAGGING -> "SCROLL_STATE_DRAGGING"
                    SCROLL_STATE_SETTLING -> "SCROLL_STATE_SETTLING"
                    else -> "SCROLL_STATE_UNKNOWN"
                })
            }
        }
    }
}
