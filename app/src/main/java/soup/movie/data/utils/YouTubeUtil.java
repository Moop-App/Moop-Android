package soup.movie.data.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public class YouTubeUtil {

    private YouTubeUtil() {}

    public enum Quality {
        LOW("default"),
        MEDIUM("mqdefault"),
        HIGH("hqdefault"),
        STANDARD("sddefault"),
        MAX("maxresdefault");

        private String mKey;

        Quality(String key) {
            mKey = key;
        }

        public String key() {
            return mKey;
        }
    }

    public static String getThumbnailUrl(@NonNull String id) {
        return getThumbnailUrl(id, Quality.STANDARD);
    }

    public static String getThumbnailUrl(@NonNull String id, Quality quality) {
        return String.format("https://img.youtube.com/vi/%s/%s.jpg", id, quality.key());
    }

    public static void executeYoutubeApp(@NonNull Context context, @NonNull String id){
        try {
            context.startActivity(createAppIntent(id));
        } catch (ActivityNotFoundException e) {
            context.startActivity(createWebIntent(id));
        }
    }

    @NonNull
    private static Intent createAppIntent(@NonNull String id) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
    }

    @NonNull
    private static Intent createWebIntent(@NonNull String id) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
    }
}
