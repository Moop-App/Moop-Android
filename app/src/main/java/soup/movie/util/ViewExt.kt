package soup.movie.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener

/** View */

@Suppress("UNCHECKED_CAST")
fun <T : View> inflate(context: Context, @LayoutRes resource: Int): T =
        View.inflate(context, resource, null) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> inflate(context: Context, @LayoutRes resource: Int, root: ViewGroup): T =
        View.inflate(context, resource, root) as T

/** ViewGroup */

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

/** ImageView */

@BindingAdapter("android:srcUrl")
fun ImageView.loadAsync(url: String) {
    Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}

fun ImageView.loadAsync(url: String, requestListener: RequestListener<Drawable>) {
    Glide.with(context)
            .load(url)
            .listener(requestListener)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}
