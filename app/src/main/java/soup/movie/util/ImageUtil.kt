package soup.movie.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener

object ImageUtil {

    @JvmStatic
    fun loadAsync(context: Context,
                  targetView: ImageView,
                  url: String) {
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(targetView)
    }

    @JvmStatic
    fun loadAsync(context: Context,
                  targetView: ImageView,
                  requestListener: RequestListener<Drawable>,
                  url: String) {
        Glide.with(context)
                .load(url)
                .listener(requestListener)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(targetView)
    }
}
