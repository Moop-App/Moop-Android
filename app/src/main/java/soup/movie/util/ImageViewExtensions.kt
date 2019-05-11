package soup.movie.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

fun ImageView.loadAsync(url: String, endAction: () -> Unit) {
    GlideApp.with(context)
        .load(url)
        .listener(createEndListener { endAction() })
        .priority(Priority.IMMEDIATE)
        .into(this)
}

private inline fun createEndListener(crossinline endAction: () -> Unit): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            endAction()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            endAction()
            return false
        }
    }
}
