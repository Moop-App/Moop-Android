package soup.widget.recyclerview.snappy

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class SnappyRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        return snappy(layoutManager)?.run {
            val targetPosition = calculateScrollPosition(velocityX, velocityY)
            Timber.d("fling: target=%d", targetPosition)
            smoothScrollToPosition(targetPosition)
            true
        } ?: super.fling(velocityX, velocityY)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        snappy(layoutManager)?.run {
            when (e.actionMasked) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        val targetPosition = getFixedScrollPosition()
                        Timber.d("onTouchEvent: target=%d", targetPosition)
                        smoothScrollToPosition(targetPosition)
                    }
                }
            }
        }
        return super.onTouchEvent(e)
    }

    private fun snappy(layoutManager: RecyclerView.LayoutManager?): SnappyLayoutManager? =
            layoutManager as? SnappyLayoutManager
}
