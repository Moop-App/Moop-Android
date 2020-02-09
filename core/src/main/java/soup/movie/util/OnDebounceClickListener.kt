package soup.movie.util

import android.view.View
import androidx.core.view.postOnAnimationDelayed
import androidx.databinding.BindingAdapter

private typealias OnClickListener = (View) -> Unit

@BindingAdapter(value = ["onDebounceClick", "onDebounceClickDelay"], requireAll = false)
fun setOnDebounceClickListener(view: View, listener: View.OnClickListener?, delay: Long = 0) {
    if (listener == null) {
        view.setOnClickListener(null)
    } else {
        view.setOnClickListener(OnDebounceClickListener {
            if (delay > 0) {
                view.postOnAnimationDelayed(delay) {
                    it.run(listener::onClick)
                }
            } else {
                it.run(listener::onClick)
            }
        })
    }
}

fun View.setOnDebounceClickListener(delay: Long = 0, listener: OnClickListener?) {
    if (listener == null) {
        setOnClickListener(null)
    } else {
        setOnClickListener(OnDebounceClickListener {
            if (delay > 0) {
                postOnAnimationDelayed(delay) {
                    run(listener)
                }
            } else {
                run(listener)
            }
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
