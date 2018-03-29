package soup.movie;

import android.app.Application;

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
    }

    public static Application getInstance() {
        return sInstance;
    }
}
