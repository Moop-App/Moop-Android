package soup.movie.ui.home

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.everything.android.ui.overscroll.OverScrollBounceEffectDecoratorBase
import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter
import kotlin.math.abs

class HomeListScrollEffect(
    recyclerView: RecyclerView
) : OverScrollBounceEffectDecoratorBase(ViewWrapper(recyclerView), -2f, 3f, 1f) {

    override fun createMotionAttributes() = MotionAttributes()

    override fun createAnimationAttributes() = AnimationAttributes()

    override fun translateView(view: View, offset: Float) {
        view.translationY = offset
    }

    override fun translateViewAndEvent(view: View, offset: Float, event: MotionEvent) {
        event.offsetLocation(offset - event.getY(0), 0f)
    }

    private class ViewWrapper(
        private val recyclerView: RecyclerView
    ) : IOverScrollDecoratorAdapter {

        override fun getView(): View {
            return recyclerView
        }

        override fun isInAbsoluteStart(): Boolean {
            return false
        }

        override fun isInAbsoluteEnd(): Boolean {
            return !recyclerView.canScrollVertically(1)
        }
    }

    protected class MotionAttributes : OverScrollBounceEffectDecoratorBase.MotionAttributes() {

        public override fun init(view: View, event: MotionEvent): Boolean {

            // We must have history available to calc the dx. Normally it's there - if it isn't temporarily,
            // we declare the event 'invalid' and expect it in consequent events.
            if (event.historySize == 0) {
                return false
            }

            // Allow for counter-orientation-direction operations (e.g. item swiping) to run fluently.
            val dy = event.getY(0) - event.getHistoricalY(0, 0)
            val dx = event.getX(0) - event.getHistoricalX(0, 0)
            if (abs(dx) > abs(dy)) {
                return false
            }

            mAbsOffset = view.translationY
            mDeltaOffset = dy
            mDir = mDeltaOffset > 0

            return true
        }
    }

    protected class AnimationAttributes : OverScrollBounceEffectDecoratorBase.AnimationAttributes() {

        init {
            mProperty = View.TRANSLATION_Y
        }

        override fun init(view: View) {
            mAbsOffset = view.translationY
            mMaxOffset = view.height.toFloat()
        }
    }
}
