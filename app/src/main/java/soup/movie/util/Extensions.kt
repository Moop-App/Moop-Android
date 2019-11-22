package soup.movie.util

import androidx.core.view.isVisible
import soup.movie.widget.ContentLoadingProgressBar

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

inline var ContentLoadingProgressBar.isInProgress: Boolean
    get() = isVisible
    set(value) {
            if (value) show() else hide()
    }
