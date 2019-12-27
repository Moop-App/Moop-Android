package soup.movie.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import soup.movie.util.glide.GlideApp
import soup.movie.util.glide.GlideRequest
import soup.movie.util.glide.IntegerKey
import soup.movie.util.helper.today
import soup.movie.util.helper.weekOfYear

/** ImageView */

@BindingAdapter(value = ["android:srcUrl", "android:srcUrlWithKey", "android:placeholder"], requireAll = false)
fun ImageView.loadAsync(url: String?, withKey: Boolean = false, placeholder: Drawable? = null) {
    if (url == null) {
        GlideApp.with(context)
            .load(placeholder)
            .into(this)
    } else {
        loadAsync(url) {
            if (withKey) {
                signature(IntegerKey(today().weekOfYear()))
            }
            if (placeholder != null) {
                placeholder(placeholder)
            }
            transition(withCrossFade())
        }
    }
}

fun ImageView.loadAsync(url: String?, withKey: Boolean, doOnEnd: () -> Unit) {
    loadAsync(url) {
        if (withKey) {
            signature(IntegerKey(today().weekOfYear()))
        }
        listener(createEndListener(doOnEnd))
    }
}

private inline fun ImageView.loadAsync(url: String?, block: GlideRequest<Drawable>.() -> Unit) {
    GlideApp.with(context)
        .load(url)
        .apply(block)
        .priority(Priority.IMMEDIATE)
        .into(this)
}

private inline fun createEndListener(crossinline action: () -> Unit): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            action()
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            action()
            return false
        }
    }
}
