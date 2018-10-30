package soup.movie.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_FORCED

object ImeUtil {

    fun showIme(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (imm != null) {
            view.requestFocus()
            imm.showSoftInput(view, SHOW_FORCED)
            imm.toggleSoftInput(SHOW_FORCED, HIDE_IMPLICIT_ONLY)
        }
    }

    fun hideIme(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (imm != null) {
            view.clearFocus()
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
