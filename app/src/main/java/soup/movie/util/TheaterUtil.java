package soup.movie.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import soup.movie.data.base.AsyncLoadListener;
import soup.movie.data.model.TheaterCode;

public class TheaterUtil {

    public static void loadAsync(@Nullable AsyncLoadListener<List<TheaterCode>> listener) {
        //TODO
        //String theaterListJson = Preference.getInstance().getString(KEY_THEATER_LIST);
        //if (StringUtils.isEmpty(theaterListJson)) {
        //} else {
        //    fireOnLoaded(listener, fromJson(theaterListJson));
        //}
    }

    private static void fireOnLoaded(@Nullable AsyncLoadListener<List<TheaterCode>> listener,
                              @NonNull List<TheaterCode> theaterCodeList) {
        if (listener != null) {
            listener.onLoaded(theaterCodeList);
        }
    }
}
