package soup.movie.settings;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections4.ListUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import soup.movie.data.model.TheaterCode;

public class TheaterSetting {

    private static final String KEY = "favorite_theaters";

    private final SharedPreferences preferences;

    public TheaterSetting(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public List<TheaterCode> getFavoriteTheaters() {
        return ListUtils.emptyIfNull(fromJson(preferences.getString(KEY, "")));
    }

    public void setFavoriteTheaters(List<TheaterCode> theaters) {
        preferences.edit().putString(KEY, toJson(theaters));
    }

    private static String toJson(List<TheaterCode> theaterCodeList) {
        return new Gson().toJson(theaterCodeList);
    }

    private static List<TheaterCode> fromJson(String jsonStr) {
        Type theaterListType = new TypeToken<ArrayList<TheaterCode>>(){}.getType();
        return new Gson().fromJson(jsonStr, theaterListType);
    }
}
