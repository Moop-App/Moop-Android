package soup.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

typealias OnInterceptTouchListener = (View, MotionEvent) -> Unit

class ConstraintLayoutEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var listener: OnInterceptTouchListener? = null

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        listener?.invoke(this, ev)
        return super.onInterceptTouchEvent(ev)
    }

    fun setOnInterceptTouchListener(l: OnInterceptTouchListener?) {
        listener = l
    }
}
