package soup.movie.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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

    private static String toJson(List<TheaterCode> theaterCodeList) {
        return new Gson().toJson(theaterCodeList);
    }

    private static List<TheaterCode> fromJson(String jsonStr) {
        Type theaterListType = new TypeToken<ArrayList<TheaterCode>>(){}.getType();
        return new Gson().fromJson(jsonStr, theaterListType);
    }

    public static void saveMyTheaterList(List<TheaterCode> theaterList) {
        //TODO
        //Preference.getInstance().putString(KEY_MY_THEATERS, toJson(theaterList));
    }

    @NonNull
    public static List<TheaterCode> getMyTheaterList() {
        return Collections.emptyList();
        //TODO
        //return ListUtils.emptyIfNull(fromJson(Preference.getInstance().getString(KEY_MY_THEATERS)));
    }
}
