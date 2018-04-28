package soup.widget.snappy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import timber.log.Timber;

public class SnappyRecyclerView extends RecyclerView {
    public SnappyRecyclerView(Context context) {
        super(context);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        SnappyLayoutManager layoutManager = mapOrNull(getLayoutManager());
        if (layoutManager != null) {
            int targetPosition = layoutManager.calculateScrollPosition(velocityX, velocityY);
            Timber.d("fling: target=%d", targetPosition);
            smoothScrollToPosition(targetPosition);
            return true;
        }
        return super.fling(velocityX, velocityY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        final boolean ret = super.onTouchEvent(e);
        SnappyLayoutManager layoutManager = mapOrNull(getLayoutManager());
        if (layoutManager != null) {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                        int targetPosition = layoutManager.getFixedScrollPosition();
                        Timber.d("onTouchEvent: target=%d", targetPosition);
                        smoothScrollToPosition(targetPosition);
                    }
                    break;
            }
        }
        return ret;
    }

    @Nullable
    private static SnappyLayoutManager mapOrNull(@Nullable LayoutManager layoutManager) {
        return layoutManager != null
                && layoutManager instanceof SnappyLayoutManager
                ? (SnappyLayoutManager) layoutManager
                : null;
    }
}
