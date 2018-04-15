package soup.movie.common.widget.snappy;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewConfiguration;

public class SnappyLinearLayoutManager extends LinearLayoutManager implements SnappyLayoutManager {

    private static final float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    private static final float INFLEXION = 0.15f; // Tension lines cross at (INFLEXION, 1)

    private static final int INVALID = -1;

    private float mFlingFriction = ViewConfiguration.getScrollFriction();
    private int mChildSize = INVALID;

    private final float mPpi;
    private float mPhysicalCoeff;

    public SnappyLinearLayoutManager(Context context) {
        this(context, VERTICAL, false);
    }

    public SnappyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPpi = context.getResources().getDisplayMetrics().density * 160.0f;
        mPhysicalCoeff = computeDeceleration(0.84f);
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
        mPhysicalCoeff = computeDeceleration(friction);
        mFlingFriction = friction;
    }

    public final void setChildSize(int size) {
        mChildSize = size;
    }

    private float computeDeceleration(float friction) {
        return SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f                   // inche/meter
                * mPpi                     // pixels per inch
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
        if (mChildSize != INVALID) {
            childSize = mChildSize;
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
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    private double getSplineFlingDistance(double velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }
}
