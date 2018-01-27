package soup.movie;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class BuildType {

    public static void init(Context context) {
        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(context);
    }

    public static void addNetworkInterceptor(OkHttpClient.Builder builder) {
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        builder.addNetworkInterceptor(new StethoInterceptor());
    }
}
