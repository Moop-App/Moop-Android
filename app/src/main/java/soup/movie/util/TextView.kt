package soup.movie.util

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("asyncText")
fun asyncText(view: TextView, text: CharSequence?) {
    if (text == null) return
    if (view is AppCompatTextView) {
        val params = TextViewCompat.getTextMetricsParams(view)
        view.setTextFuture(PrecomputedTextCompat.getTextFuture(text, params, null))
    }
}
