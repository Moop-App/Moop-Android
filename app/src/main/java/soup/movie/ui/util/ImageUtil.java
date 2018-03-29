package soup.movie.ui.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.reactivex.annotations.NonNull;

public class ImageUtil {

    private ImageUtil() {}

    public static void loadAsync(@NonNull Context context,
                                 @NonNull ImageView targetView,
                                 @NonNull String url) {
        Glide.with(context).load(url).into(targetView);
    }
}
