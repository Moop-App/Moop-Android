package soup.movie.data.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import soup.movie.data.core.AsyncLoadListener;
import soup.movie.data.soup.model.Area;
import soup.movie.data.soup.model.CodeRequest;
import soup.movie.data.soup.model.CodeResponse;
import soup.movie.data.soup.model.TheaterCode;
import timber.log.Timber;

public class TheaterUtil {

    private static final Key<String> PREF_KEY = new InerasableKey<>("theater_list", "");

    public static void loadAsync(@Nullable AsyncLoadListener<List<TheaterCode>> listener) {
        String theaterListJson = Preference.getInstance().getString(PREF_KEY);
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
                        Preference.getInstance().putString(PREF_KEY, toJson(codes));
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
}
