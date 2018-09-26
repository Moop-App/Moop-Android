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
import soup.movie.util.Interpolators.ALPHA_IN
import soup.movie.util.Interpolators.ALPHA_OUT

/** View */

@Suppress("UNCHECKED_CAST")
fun <T : View> inflate(context: Context, @LayoutRes resource: Int): T =
        View.inflate(context, resource, null) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> inflate(context: Context, @LayoutRes resource: Int, root: ViewGroup): T =
        View.inflate(context, resource, root) as T

inline fun <T : View> T.setVisibleIf(predicate: () -> Boolean) {
    visibility = if (predicate()) View.VISIBLE else View.GONE
}

inline fun <T : View> T.setGoneIf(predicate: () -> Boolean) {
    visibility = if (predicate()) View.GONE else View.VISIBLE
}

fun View.animateHide(animate: Boolean) {
    animate().cancel()
    if (!animate) {
        alpha = 0f
        visibility = View.INVISIBLE
        return
    }
    animate()
            .alpha(0f)
            .setDuration(160)
            .setStartDelay(0)
            .setInterpolator(ALPHA_OUT)
            .withEndAction { visibility = View.INVISIBLE }
}

fun View.animateShow(animate: Boolean) {
    animate().cancel()
    visibility = View.VISIBLE
    if (!animate) {
        alpha = 1f
        return
    }
    alpha = 0f
    animate()
            .alpha(1f)
            .setDuration(320)
            .setInterpolator(ALPHA_IN)
            .setStartDelay(50)
            // We need to clean up any pending end action from animateHide if we call
            // both hide and show in the same frame before the animation actually gets started.
            // cancel() doesn't really remove the end action.
            .withEndAction(null)
}

@BindingAdapter("android:backgroundRes")
fun View.setBackgroundRes(resId: Int) {
    setBackgroundResource(resId)
}

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
