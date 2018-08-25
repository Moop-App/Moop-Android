package soup.movie.ui.main;

import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;

public interface OnInternalClickListener {

    void onItemClick(int position, @Nullable Pair<View, String>... sharedElements);
}
