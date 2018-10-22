package soup.widget.util

import android.view.View

class OnDebounceClickListener(private val listener: (View?) -> Unit) : View.OnClickListener {

    override fun onClick(v: View?) {
        val now = System.currentTimeMillis()
        if (now - lastTime < INTERVAL) return
        lastTime = now
        listener.invoke(v)
    }

    companion object {

        private const val INTERVAL: Long = 300

        private var lastTime: Long = 0
    }
}
