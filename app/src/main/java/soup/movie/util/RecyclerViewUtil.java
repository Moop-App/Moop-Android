package soup.movie.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

public class RecyclerViewUtil {

    private RecyclerViewUtil() {}

    public static LinearLayoutManager createLinearLayoutManager(@NonNull Context context) {
        return new LinearLayoutManager(context);
    }

    public static LinearLayoutManager createLinearLayoutManager(@NonNull Context context,
                                                                boolean vertical) {
        LinearLayoutManager layoutManager = createLinearLayoutManager(context);
        layoutManager.setOrientation(vertical
                ? LinearLayoutManager.VERTICAL
                : LinearLayoutManager.HORIZONTAL);
        return layoutManager;
    }

    public static GridLayoutManager createGridLayoutManager(@NonNull Context context,
                                                            int spanCount) {
        return new GridLayoutManager(context, spanCount);
    }
}
