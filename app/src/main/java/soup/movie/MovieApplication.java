package soup.movie;

import android.app.Application;

public class MovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BuildType.init(this);
    }
}
