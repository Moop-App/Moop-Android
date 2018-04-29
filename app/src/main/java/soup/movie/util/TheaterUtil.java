package soup.movie.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.core.preference.Preference;
import soup.movie.core.preference.key.InerasableKey;
import soup.movie.core.preference.key.Key;
import soup.movie.data.base.AsyncLoadListener;
import soup.movie.data.model.Area;
import soup.movie.data.model.CodeRequest;
import soup.movie.data.model.CodeResponse;
import soup.movie.data.model.TheaterCode;
import timber.log.Timber;

public class TheaterUtil {

    private static final Key<String> KEY_THEATER_LIST = new InerasableKey<>("theater_list", "");

    private static final Key<String> KEY_MY_THEATERS = new InerasableKey<>("my_theaters", "");

    public static void loadAsync(@Nullable AsyncLoadListener<List<TheaterCode>> listener) {
        String theaterListJson = Preference.getInstance().getString(KEY_THEATER_LIST);
        if (StringUtils.isEmpty(theaterListJson)) {
            //TODO:
            Disposable disposable = Injection.get()
                    .getMovieRepository()
                    .getCodeList(new CodeRequest())
                    .map(CodeResponse::getList)
                    .map(areas -> {
                        List<TheaterCode> codes = new ArrayList<>();
                        for (Area area : areas) {
                            codes.addAll(area.getTheaterList());
                        }
                        Timber.d("loadAsync: complete, data=%s", codes);
                        Preference.getInstance().putString(KEY_THEATER_LIST, toJson(codes));
                        return codes;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(theaters -> fireOnLoaded(listener, theaters), Timber::e);
        } else {
            fireOnLoaded(listener, fromJson(theaterListJson));
        }
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
        Preference.getInstance().putString(KEY_MY_THEATERS, toJson(theaterList));
    }

    @NonNull
    public static List<TheaterCode> getMyTheaterList() {
        return ListUtils.emptyIfNull(fromJson(Preference.getInstance().getString(KEY_MY_THEATERS)));
    }
}
