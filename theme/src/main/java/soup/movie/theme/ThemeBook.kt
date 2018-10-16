package soup.movie.theme

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import soup.movie.theme.bookmark.ThemeBookmarkActivity

object ThemeBook {

    fun initialize(application: Application, default: ThemePage, vararg themes: ThemePage) {
        ThemeBookImpl.makeThemeBook(application, default, *themes)
    }

    fun open(activity: Activity) {
        ThemeBookImpl.open(activity)
    }

    fun turnOver(activity: Activity, page: ThemePage) {
        ThemeBookImpl.turnOver(activity, page)
    }

    fun getAvailablePages(): List<ThemePage> {
        return ThemeBookImpl.getAvailablePages()
    }

    fun getBookmarkPage(): ThemePage {
        return ThemeBookImpl.getBookmarkPage()
    }

    fun executeBookmark(ctx: Context) {
        ctx.startActivity(Intent(ctx, ThemeBookmarkActivity::class.java))
    }
}
