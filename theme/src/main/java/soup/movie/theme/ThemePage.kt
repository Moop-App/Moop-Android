package soup.movie.theme

import android.app.Activity
import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import soup.movie.theme.util.getColorAccent
import soup.movie.theme.util.getColorPrimary
import soup.movie.theme.util.getColorPrimaryDark

abstract class ThemePage(val id: String, val name: String) {

    @StyleRes
    abstract fun extractThemeOf(activity: Activity?): Int

    @StyleRes
    fun extractDefaultTheme(): Int {
        return extractThemeOf(null)
    }

    @ColorInt
    fun getColorPrimary(ctx: Context): Int {
        return ctx.getColorPrimary(extractDefaultTheme())
    }

    @ColorInt
    fun getColorPrimaryDark(ctx: Context): Int {
        return ctx.getColorPrimaryDark(extractDefaultTheme())
    }

    @ColorInt
    fun getColorAccent(ctx: Context): Int {
        return ctx.getColorAccent(extractDefaultTheme())
    }

    fun isBookmarked(): Boolean {
        return ThemeBook.getBookmarkPage().id == id
    }
}
