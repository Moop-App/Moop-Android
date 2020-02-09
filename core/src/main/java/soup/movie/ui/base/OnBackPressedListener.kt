package soup.movie.ui.base

import androidx.fragment.app.Fragment

interface OnBackPressedListener {

    fun onBackPressed(): Boolean
}

fun Fragment?.consumeBackEvent(): Boolean {
    if (this == null) {
        return false
    }
    if (this is OnBackPressedListener) {
        return onBackPressed()
    }
    return false
}

fun Fragment.consumeBackEventInChildFragment(): Boolean {
    return childFragmentManager.fragments.elementAtOrNull(0).consumeBackEvent()
}
