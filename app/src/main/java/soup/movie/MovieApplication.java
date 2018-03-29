package soup.movie;

import android.app.Application;

import soup.movie.data.utils.TheaterUtil;

public class MovieApplication extends Application {

    private static Application sInstance;

    public MovieApplication() {
        super();
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BuildType.init(this);
        TheaterUtil.loadAsync(null); // pre-load
    }

    public static Application getInstance() {
        return sInstance;
    }
}
