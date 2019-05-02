package soup.movie.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import soup.movie.util.glide.GlideApp

/** ImageView */

@BindingAdapter("android:srcUrl")
fun ImageView.loadAsync(url: String) {
    GlideApp.with(context)
        .load(url)
        .priority(Priority.IMMEDIATE)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadAsync(url: String, requestListener: RequestListener<Drawable>) {
    GlideApp.with(context)
        .load(url)
        .listener(requestListener)
        .priority(Priority.IMMEDIATE)
        .into(this)
}
