package soup.movie.ui.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;

import io.reactivex.annotations.NonNull;

public class ImageUtil {

    private ImageUtil() {}

    public static void loadAsync(@NonNull Context context,
                                 @NonNull ImageView targetView,
                                 @NonNull String url) {
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(targetView);
    }

    public static void loadAsync(@NonNull Context context,
                                 @NonNull ImageView targetView,
                                 @NonNull RequestListener<Drawable> requestListener,
                                 @NonNull String url) {
        Glide.with(context)
                .load(url)
                .listener(requestListener)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(targetView);
    }
}
