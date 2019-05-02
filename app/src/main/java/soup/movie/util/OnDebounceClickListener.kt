package soup.movie.util

import android.view.View
import androidx.databinding.BindingAdapter

private typealias OnClickListener = (View) -> Unit

@BindingAdapter("onDebounceClick")
fun View.setOnDebounceClickListener(listener: View.OnClickListener?) {
    if (listener == null) {
        setOnClickListener(null)
    } else {
        setOnClickListener(OnDebounceClickListener {
            it.run(listener::onClick)
        })
    }
}

fun View.setOnDebounceClickListener(listener: OnClickListener?) {
    if (listener == null) {
        setOnClickListener(null)
    } else {
        setOnClickListener(OnDebounceClickListener {
            it.run(listener)
        })
    }
}

class OnDebounceClickListener(private val listener: OnClickListener) : View.OnClickListener {

    override fun onClick(v: View?) {
        val now = System.currentTimeMillis()
        if (now - lastTime < INTERVAL) return
        lastTime = now
        if (v != null) {
            listener(v)
        }
    }

    companion object {

        private const val INTERVAL: Long = 300

        private var lastTime: Long = 0
    }
}
