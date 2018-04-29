package soup.movie.settings;

import android.content.SharedPreferences;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HomeTypeSetting {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HOME_TYPE_HORIZONTAL, HOME_TYPE_VERTICAL})
    private @interface HomeType {
    }
    public static final int HOME_TYPE_VERTICAL = 1;
    public static final int HOME_TYPE_HORIZONTAL = 2;

    private static final String KEY = "home_type";
    private static final int DEFAULT_VALUE = HOME_TYPE_VERTICAL;

    private final SharedPreferences preferences;

    public HomeTypeSetting(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public @HomeType int getHomeType() {
        return preferences.getInt(KEY, DEFAULT_VALUE);
    }

    public boolean isVerticalType() {
        return getHomeType() == HOME_TYPE_VERTICAL;
    }

    public boolean isHorizontalType() {
        return getHomeType() == HOME_TYPE_HORIZONTAL;
    }

    public void setHomeType(@HomeType int homeType) {
        preferences.edit().putInt(KEY, homeType).apply();
    }

    public void setVerticalType() {
        setHomeType(HOME_TYPE_VERTICAL);
    }

    public void setHorizontalType() {
        setHomeType(HOME_TYPE_HORIZONTAL);
    }
}
