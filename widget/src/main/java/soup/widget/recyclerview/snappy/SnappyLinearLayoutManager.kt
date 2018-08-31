package soup.widget.recyclerview.snappy

import android.content.Context
import android.graphics.PointF
import android.hardware.SensorManager
import android.view.ViewConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class SnappyLinearLayoutManager(context: Context,
                                orientation: Int = RecyclerView.VERTICAL,
                                reverseLayout: Boolean = false) :
        LinearLayoutManager(context, orientation, reverseLayout), SnappyLayoutManager {

    private var flingFriction: Double = ViewConfiguration.getScrollFriction().toDouble()
    private var childSize: Int = INVALID

    private val ppi: Double = context.resources.displayMetrics.density * 160.0
    private var physicalCoeff: Double = 0.toDouble()

    init {
        physicalCoeff = computeDeceleration(0.84)
    }

    fun vertically(): SnappyLinearLayoutManager {
        orientation = RecyclerView.VERTICAL
        return this
    }

    fun horizontally(): SnappyLinearLayoutManager {
        orientation = RecyclerView.HORIZONTAL
        return this
    }

    fun setFriction(friction: Double) {
        physicalCoeff = computeDeceleration(friction)
        flingFriction = friction
    }

    fun setChildSize(size: Int) {
        childSize = size
    }

    private fun computeDeceleration(friction: Double): Double {
        return (SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f                    // inche/meter
                * ppi                       // pixels per inch
                * friction)
    }

    override fun calculateScrollPosition(velocityX: Int, velocityY: Int): Int {
        if (childCount > 0) {
            getChildAt(0)?.run {
                if (orientation == RecyclerView.HORIZONTAL) {
                    return calculateScrollPositionForVelocity(
                            velocityX, left, width, getPosition(this))
                } else {
                    return calculateScrollPositionForVelocity(
                            velocityY, top, height, getPosition(this))
                }
            }
        }
        return 0
    }

    private fun calculateScrollPositionForVelocity(velocity: Int, scrollPosition: Int, childSize: Int,
                                                   currPosition: Int): Int {
        val adjustChildSize =
                if (this.childSize != INVALID) this.childSize
                else childSize
        val dist = getSplineFlingDistance(velocity.toDouble())
        val tempScroll = scrollPosition + if (velocity > 0) dist else -dist

        return if (velocity < 0) {
            Math.max(currPosition + tempScroll / adjustChildSize, 0.0).toInt()
        } else {
            (currPosition.toDouble() + tempScroll / adjustChildSize + 1.0).toInt()
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {

            override fun getHorizontalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }

            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }

            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@SnappyLinearLayoutManager
                        .computeScrollVectorForPosition(targetPosition)
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    override fun getFixedScrollPosition(): Int {
        if (this.childCount == 0) {
            return 0
        }
        val child = getChildAt(0)
        var childPosition = getPosition(child!!)

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (Math.abs(child.left) > child.measuredWidth / 2) {
                childPosition += 1
            }
        } else {
            if (Math.abs(child.top) > child.measuredWidth / 2) {
                childPosition += 1
            }
        }
        return childPosition
    }

    private fun getSplineDeceleration(velocity: Double): Double =
            Math.log(INFLEXION * Math.abs(velocity) / (flingFriction * physicalCoeff))

    private fun getSplineFlingDistance(velocity: Double): Double {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne = DECELERATION_RATE - 1.0
        return flingFriction * physicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l)
    }

    companion object {

        private val DECELERATION_RATE = (Math.log(0.78) / Math.log(0.9)).toFloat()
        private const val INFLEXION = 0.15f // Tension lines cross at (INFLEXION, 1)

        private const val INVALID = -1
    }
}
