package soup.movie.util

import android.view.View
import androidx.core.view.postOnAnimationDelayed

private typealias OnClickListener = (View) -> Unit

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
