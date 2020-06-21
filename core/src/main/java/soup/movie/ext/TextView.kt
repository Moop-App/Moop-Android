package soup.movie.ext

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat

fun TextView.asyncText(text: CharSequence?) {
    if (text == null) return
    if (this is AppCompatTextView) {
        val params = TextViewCompat.getTextMetricsParams(this)
        this.setTextFuture(PrecomputedTextCompat.getTextFuture(text, params, null))
    }
}
