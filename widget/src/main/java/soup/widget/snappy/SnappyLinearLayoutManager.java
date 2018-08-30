package soup.widget.snappy;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class SnappyLinearLayoutManager extends LinearLayoutManager implements SnappyLayoutManager {

    private static final float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    private static final float INFLEXION = 0.15f; // Tension lines cross at (INFLEXION, 1)

    private static final int INVALID = -1;

    private float flingFriction = ViewConfiguration.getScrollFriction();
    private int childSize = INVALID;

    private final float ppi;
    private float physicalCoeff;

    public SnappyLinearLayoutManager(Context context) {
        this(context, VERTICAL, false);
    }

    public SnappyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        physicalCoeff = computeDeceleration(0.84f);
    }

    public final SnappyLinearLayoutManager vertically() {
        setOrientation(VERTICAL);
        return this;
    }

    public final SnappyLinearLayoutManager horizontally() {
        setOrientation(HORIZONTAL);
        return this;
    }

    public final void setFriction(float friction) {
        physicalCoeff = computeDeceleration(friction);
        flingFriction = friction;
    }

    public final void setChildSize(int size) {
        childSize = size;
    }

    private float computeDeceleration(float friction) {
        return SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f                   // inche/meter
                * ppi                     // pixels per inch
                * friction;
    }

    @Override
    public int calculateScrollPosition(int velocityX, int velocityY) {
        if (getChildCount() == 0) {
            return 0;
        }
        final View child = getChildAt(0);
        final int childPosition = getPosition(child);

        if (getOrientation() == LinearLayoutManager.HORIZONTAL) {
            return calculateScrollPositionForVelocity(
                    velocityX, child.getLeft(), child.getWidth(), childPosition);
        } else {
            return calculateScrollPositionForVelocity(
                    velocityY, child.getTop(), child.getHeight(), childPosition);
        }
    }

    private int calculateScrollPositionForVelocity(int velocity, int scrollPosition, int childSize,
                                                   int currPosition) {
        if (this.childSize != INVALID) {
            childSize = this.childSize;
        }
        final double dist = getSplineFlingDistance(velocity);
        final double tempScroll = scrollPosition + (velocity > 0 ? dist : -dist);

        if (velocity < 0) {
            return (int) Math.max(currPosition + (tempScroll / childSize), 0);
        } else {
            return (int) (currPosition + (tempScroll / childSize) + 1);
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {

                    protected int getHorizontalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }

                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }

                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return SnappyLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    @Override
    public int getFixedScrollPosition() {
        if (this.getChildCount() == 0) {
            return 0;
        }
        final View child = getChildAt(0);
        int childPosition = getPosition(child);

        if (getOrientation() == LinearLayoutManager.HORIZONTAL) {
            if (Math.abs(child.getLeft()) > child.getMeasuredWidth() / 2) {
                childPosition += 1;
            }
        } else {
            if (Math.abs(child.getTop()) > child.getMeasuredWidth() / 2) {
                childPosition += 1;
            }
        }
        return childPosition;
    }

    private double getSplineDeceleration(double velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (flingFriction * physicalCoeff));
    }

    private double getSplineFlingDistance(double velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return flingFriction * physicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }
}
